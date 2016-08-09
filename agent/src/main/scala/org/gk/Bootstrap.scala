package org.gk

import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.gk.api.{HeartApi, MonitorApi}
import org.gk.services.base.DatabaseServices
import scala.concurrent.Future
import scala.io.StdIn
import scala.util.{Failure, Success}

/**
  * Created by zhangxu on 2016/8/1.
  */
object Bootstrap {

  import org.gk.services.base.ActorSystemServices._
  import org.gk.common.MyType._

  private val log = Logging(this)

  val routes: Route = HeartApi.route ~ MonitorApi.route
  val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(routes, "0.0.0.0", 8083)
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")

  def main(args: Array[String]): Unit = {
    DatabaseServices.initDatabase.onComplete {
      case Success(_) =>
        StdIn.readLine()
        log.info("Init Database Success.")
      case Failure(ex) => log.info(s"Init Database Failure: ${ex.getMessage}")
    }
  }


}
