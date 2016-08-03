/**
  * Created by zhangxu on 2016/8/1.
  */
package org.gk.services

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

object HeartService {
  val route = (get & path("api" / "heart")) (complete(HttpResponse()))
}
