package org.gk

import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server._
import org.gk.api.NodeInfo
import org.gk.services.{HeartService, MonitorService}

import scala.concurrent.Future

/**
  * Created by zhangxu on 2016/8/1.
  */
object Bootstrap {

  import org.gk.services.base.ActorSystemServices._

  val routes: Route = NodeInfo.route

  val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(routes, "0.0.0.0", 8082)

  def main(args: Array[String]) {
    new HeartService().start
    new MonitorService().start
  }
}
