package org.gk

import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.gk.api.{HeartApi, MonitorApi}

import scala.concurrent.Future
import scala.io.StdIn

/**
  * Created by zhangxu on 2016/8/1.
  */
object Bootstrap extends App {

  import org.gk.services.base.ActorSystemServices._

  val routes: Route = HeartApi.route ~ MonitorApi.route

  val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(routes, "0.0.0.0", 8081)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()
  //  bindingFuture
  //    .flatMap(_.unbind())
  //    .onComplete(_ => system.terminate())
}
