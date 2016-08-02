/**
  * Created by zhangxu on 2016/8/1.
  */
package service

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

object RestService {
  val route = (get & path("api" / "heart")) {
    println(111)
    complete(HttpResponse())}
}
