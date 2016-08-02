/**
  * Created by zhangxu on 2016/8/1.
  */
package service

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source, Unzip, Zip}
import common.MyType
import service.HeartService.Heart
import service.base.{ClientServices, DatabaseSerivces}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

object HeartService {

  case class Heart(ip: String, status: Boolean)

}

class HeartService(implicit system: ActorSystem) {

  import MyType._
  import akka.event.Logging

  val log = Logging(system, this)

  val start = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val genNodeIp =
      Source.tick(initialDelay = 0.second, interval = 1.second, ())
      .mapAsync(1)(_getMonitorNodes(_))
      .mapConcat(_:::Nil)

    val checkHeart = Flow[String].mapAsync(1)(_genStatusOfWSResponse(_))

    val makeHeartObj = Flow[(String,Boolean)].map(p=>Heart(p._1,p._2))

    val filter = Flow[Heart]
      .mapAsync(1)(p=>_updateDBNodeHeartTime(p.ip))

    val filterNot = Flow[Heart]
      .map(p=>updateNodeHeatFailreMap(p.ip))
      .filter(nodeHeatFailreMap(_) > 5)
      .mapAsync(1)(_updateDBNodeDie(_))

    val broadcast = b.add(Broadcast[String](2))
    val bcast = b.add(Broadcast[Heart](2))
    val merge = b.add(Merge[Any](2))
    val zip = b.add(Zip[String,Boolean])


     genNodeIp ~> broadcast ~> checkHeart ~> zip.in1

                  broadcast               ~> zip.in0

                 bcast.in <~ makeHeartObj <~ zip.out

                 bcast.out(0).filter(_.status)  ~> filter    ~> merge

                 bcast.out(1).filter(!_.status) ~> filterNot ~> merge ~> Sink.ignore


    ClosedShape
  })

  private val _genStatusOfWSResponse = (ip: String) =>
    ClientServices.heartService(ip)
      .map(p =>  true)
      .recover {
        case ex: Exception =>
          log.error(s"node heart failure: ${ex.getMessage}.")
          false
      }

  private val _updateDBNodeHeartTime = (ip: String) => {
    log.info(s"node { $ip } heart success.")
    DatabaseSerivces.updateNodeHeartTime(ip)
  }

  private val _getMonitorNodes: (Unit) => Future[List[String]] = (u: Unit) => {
    val or = if (System.currentTimeMillis() / 1000 % 15 == 0) Some {
      log.info("test heart die node.")
      DatabaseSerivces.updateDieToRetryNodes
    } else None

    val gthn: Future[List[String]] = DatabaseSerivces.getTestHeartNodes

    or.map(_.flatMap(p => gthn)).getOrElse(gthn)
  }

  import scala.collection.mutable.Map

  private val nodeHeatFailreMap: Map[String, Int] = Map.empty

  private val updateNodeHeatFailreMap = (ip: String) => {
    nodeHeatFailreMap.get(ip) match {
      case Some(e) =>
        log.info(s"node { $ip } retry number: ${e + 1}")
        nodeHeatFailreMap.update(ip, e + 1)
      case None =>
        log.info(s"node { $ip } into retry list")
        nodeHeatFailreMap += (ip -> 0)
    }

    log.info(s"node { $ip } heart failure.")

    ip
  }

  private val _updateDBNodeDie = (ip: String) => {
    nodeHeatFailreMap -= (ip)
    log.info(s"node { $ip } heart die.")
    DatabaseSerivces.updateNodeHeartDie(ip)
      .map(p => ip)
  }
}