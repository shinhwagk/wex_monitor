package org.gk.api

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import org.gk.services.base.DatabaseServices

/**
  * Created by zhangxu on 2016/8/8.
  */
object MonitorApi {
  val route = (get & path("api" / "monitor" / Remaining)) { task_name =>
    onSuccess(DatabaseServices._count_task(task_name)) { cnt =>
      if (cnt == 0) {
        complete(HttpResponse(StatusCodes.NotFound))
      } else {
        complete(HttpResponse(StatusCodes.OK))
      }
    }
  }
}
