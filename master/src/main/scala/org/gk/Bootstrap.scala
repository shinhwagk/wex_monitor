package org.gk

import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server._
import org.gk.api.NodeInfo
import org.gk.services.base.DatabaseSerivces
import org.gk.services.{HeartService, MonitorService}

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by zhangxu on 2016/8/1.
  */
object Bootstrap {

  import org.gk.services.base.ActorSystemServices._

  val routes: Route = NodeInfo.route

  val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(routes, "0.0.0.0", 8082)

  import org.gk.common.MyType._

  private val log = Logging(this)

  def main(args: Array[String]) {
    DatabaseSerivces.initDatabase.onComplete {
      case Success(_) =>
        new HeartService().start
        new MonitorService().start
        log.info("Init Database Success.")
      case Failure(ex) => log.info(s"Init Database Failure: ${ex.getMessage}")
    }

  }
}
