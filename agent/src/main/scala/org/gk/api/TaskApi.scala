package org.gk.api

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives._
import org.gk.services.base.{DatabaseServices, Task}

/**
  * Created by zhangxu on 2016/8/8.
  */
object TaskApi {

  import org.gk.services.base.DataTransform._

  val route =
    path("api" / "task") {
      get {
        complete("a")
      }
      post {
        entity(as[Task]) { task: Task =>
          onSuccess(DatabaseServices._insert_task(task)) { none =>
            complete(HttpResponse())
          }
        }
      }
    }

}
