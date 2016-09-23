/**
  * Created by zhangxu on 2016/8/1.
  */
package org.gk.services

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream._
import akka.stream.scaladsl.{Sink, Source}
import org.gk.common.Configure
import org.gk.common.MyType._
import org.gk.services.base.DatabaseSerivces
import org.gk.services.base.DatabaseSerivces._
import org.gk.services.base.DatabaseTables.{Node, NodeStatus}
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object HeartService {

  val _bufferSize = 100
  val _heart_die_retry_interval = Configure.heart_die_retry_interval
  val _heart_interval = Configure.heart_interval * 1000
  val _heart_retry = Configure.heart_retry

  val log = Logging(this)

  val _gen_query = () =>
    if (System.currentTimeMillis() / 1000 % _heart_die_retry_interval == 0)
      _nodes_Table.filter(n => n.status =!= NodeStatus.STOP)
    else
      _nodes_Table.filter(n => n.status =!= NodeStatus.STOP && n.status =!= NodeStatus.DIE)
}

class HeartService(implicit system: ActorSystem, materializer: ActorMaterializer) {

  import HeartService._

  private val _node_check: (Node) => Unit = (node: Node) => {
    val ip = node.ip
    //    val connectionFlow = Http().outgoingConnection(ip, node.port)
    //    val responseFuture: Future[Done] = Source.single(HttpRequest(uri = "/api/agent/heart"))
    //      .via(connectionFlow)
    //      .runWith(Sink.ignore)
    import scala.sys.process._
    val responseFuture: Future[String] = Future(Seq("ping", "-c", "4", ip).!!)

    responseFuture.onComplete {
      case Success(_) =>
        val n: Node = node.zeroRetry.setStatus(NodeStatus.NORMAL).updateTimestamp
        DatabaseSerivces.updateNode(n)
          .foreach(p => log.debug(s"node update: ${n}"))
        log.info(s"node: $ip heart success.")

      case Failure(ex) =>
        if (node.retry + 1 > _heart_retry) {
          val n: Node = node.zeroRetry.setStatus(NodeStatus.DIE).updateTimestamp
          DatabaseSerivces.updateNode(n).foreach(none => log.debug(s"node update: ${n}"))
          log.info(s"node: $ip die.")
        } else {
          val n: Node = node.incRetry.setStatus(NodeStatus.DOUBT).updateTimestamp
          DatabaseSerivces.updateNode(n).foreach(none => log.debug(s"node update: ${n}"))
          log.info(s"update node: $ip retry number: ${n.retry}")
        }

        log.error(s"node: ${ip} heart failure: ${ex.getMessage}.")
    }
  }

  val _queue = Source.queue[Node](_bufferSize, OverflowStrategy.dropNew)
    .map(_node_check(_)).async
    .to(Sink.ignore).run()

  val _nodes_check = () => {
    import DatabaseSerivces._

    val query = _gen_query.apply

    Source.fromPublisher(db.stream(query.result))
      .runForeach(_queue.offer(_))
      .onComplete {
        case Success(_) => log.debug("heart service: queue send success")
        case Failure(ex) => log.debug(s"heart service: queue send failure: ${ex.getMessage}")
      }
  }

  val start = {
    Future {
      while (true) {
        _nodes_check.apply
        Thread.sleep(_heart_interval)
      }
    }
  }
}