/**
  * Created by zhangxu on 2016/8/1.
  */
package service

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.Merge
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source}
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

    val timer =
      Source.tick(initialDelay = 0.second, interval = 1.second, ())

    val genNodes = Flow[Unit]
      .mapAsync(1)(_getNormalNodes(_))
      .mapConcat(p => p)
      .mapAsync(10)(_genStatusOfWSResponse(_))

    val filter = Flow[Heart]
      .filter(_.status)
      .map(_.ip)
      .mapAsync(1)(_updateDBNodeHeartTime(_))

    val filterNot = Flow[Heart]
      .filter(!_.status)
      .map(_.ip)
      .map(updateNodeHeatFailreMap(_))
      .filter(nodeHeatFailreMap(_) > 5)
      .mapAsync(1)(_updateDBNodeDie(_))

    val broadcast = b.add(Broadcast[Heart](2))
    val merge = b.add(Merge[Any](2))


    timer ~> genNodes ~> broadcast ~> filter    ~> merge

                         broadcast ~> filterNot ~> merge ~> Sink.ignore


    ClosedShape
  })

  private val _genStatusOfWSResponse = (ip: String) =>
    ClientServices.heartService(ip)
      .map(p => Heart(ip, true))
      .recover {
        case ex: Exception =>
          log.error(s"node heart failure: ${ex.getMessage}.")
          Heart(ip, false)
      }

  private val _updateDBNodeHeartTime = (ip: String) => {
    log.info(s"node { $ip } heart success.")
    DatabaseSerivces.updateNodeHeartTime(ip)
  }

  private val _getNormalNodes: (Unit) => Future[List[String]] = (u: Unit) => {
    val or = if (System.currentTimeMillis() / 1000 % 15 == 0) Some {
      log.info("test heart die node.")
      DatabaseSerivces.updateNodeHeartRetry
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