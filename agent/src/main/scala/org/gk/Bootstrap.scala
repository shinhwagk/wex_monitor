package org.gk

import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import org.gk.services.HeartService
import scala.concurrent.Future
import scala.io.StdIn

/**
  * Created by zhangxu on 2016/8/1.
  */
object Bootstrap extends App {
  import org.gk.services.base.ActorSystemServices._

  val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(HeartService.route, "0.0.0.0", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()
//  bindingFuture
//    .flatMap(_.unbind())
//    .onComplete(_ => system.terminate())
}
