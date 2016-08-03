/**
  * Created by zhangxu on 2016/8/1.
  */
package org.gk.services

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source, Zip}
import org.gk.common.{Configure, MyType}
import org.gk.services.HeartService.Heart
import org.gk.services.base.{ClientServices, DatabaseSerivces}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object HeartService {

  object Heart{
    def apply(d:(String,Boolean)):Heart = Heart(d._1,d._2)
  }

  case class Heart(ip:String, status:Boolean, retry :Int = 0){
    def setRetry(cnt:Int) = Heart(ip,status,cnt)
  }

}

class HeartService(implicit system: ActorSystem) {

  import MyType._
  import akka.event.Logging

  val heart_interval = Configure.heart_interval
  val log = Logging(system, this)

  val start = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val genNodeIp =
      Source.tick(initialDelay = 0.second, interval = heart_interval.second, ())
        .mapAsync(1)(_getMonitorNodes(_))
        .mapConcat(_ ::: Nil)

    val checkHeart = Flow[String].mapAsync(4)(_checkHeartService(_))

    val success = Flow[Heart]
      .filter(_.status)
      .mapAsync(1)(_updateDBNodeHeartTime(_))

    val failure = Flow[Heart]
      .filter(!_.status)
      .mapAsync(1)(_updateNodeHeatFailure(_))
      .mapAsync(1)(_getNodeRetryCount(_))
      .filter(_.retry >= 5)
      .mapAsync(1)(_updateDBNodeDie(_))

    val broadcast = b.add(Broadcast[String](2))
    val bcast = b.add(Broadcast[Heart](2))
    val merge = b.add(Merge[Any](2))
    val zip = b.add(Zip[String, Boolean])


    genNodeIp ~> broadcast ~> checkHeart ~> zip.in1

                 broadcast               ~> zip.in0

    zip.out.map(Heart(_)) ~> bcast.in

                             bcast.out(0) ~> success ~> merge

                             bcast.out(1) ~> failure ~> merge ~> Sink.ignore


    ClosedShape
  })

  private val _checkHeartService = (ip: String) => {
    log.info(s"node: ${ip} heart check.")
    val startTimestamp:Long = System.currentTimeMillis
    ClientServices.heartCheckService(ip)
      .map(_ => true)
      .recover {
        case ex: Exception =>
          log.error(s"node: ${ip} heart failure: ${ex.getMessage}.")
          false
      }.map{ boolean =>
        log.info(s"node: ${ip} heart check used time: ${System.currentTimeMillis() - startTimestamp} Millis")
        boolean
      }
  }
  private val _updateDBNodeHeartTime = (h: Heart) => {
    log.info(s"node: ${h.ip} heart success.")
    DatabaseSerivces.updateNodeHeartTimestamp(h.ip)
  }

  private val _getMonitorNodes = (u: Unit) => {
    val hri = Configure.die_retry_interval
    val or = if (System.currentTimeMillis() / 1000 % hri == 0) Some {
      log.info("retry check die node heart.")
      DatabaseSerivces.updateNodeStatusToDoubt
    } else None

    val sthn = DatabaseSerivces.selectCheckHeartNodes

    or.map(_.flatMap(_ => sthn)).getOrElse(sthn)
  }

  private val _getNodeRetryCount = (h: Heart) => {
    val ip = h.ip
    DatabaseSerivces.selectNodeHeartRetryCount(ip).map{cnt:Int=>
      log.info(s"node { $ip } retry number: ${cnt}")
      h.setRetry(cnt)
    }
  }
  
  private val _updateNodeHeatFailure = (h: Heart) => {
    val ip  = h.ip
    log.info(s"node { $ip } heart failure.")
    DatabaseSerivces.updateNodeHeartRetryInc(ip).map(_=>h)
  }

  private val _updateDBNodeDie = (h: Heart) => {
    val ip = h.ip
    log.info(s"node { $ip } heart die.")
    DatabaseSerivces.updateNodeStatusToDie(ip)
      .flatMap(_=>DatabaseSerivces.updateNodeHeartRetryZero(ip))
      .map(_ => ip)
  }

}