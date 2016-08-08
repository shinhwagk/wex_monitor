package org.gk.api

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import org.gk.services.base.DatabaseServices
import slick.driver.H2Driver.api._

/**
  * Created by zhangxu on 2016/8/8.
  */
object MonitorApi {

  import DatabaseServices._

  val route = (get & path("api" / "agent" / "monitor" / Segment / Segment)) { (node_ip, task_name) =>
    onSuccess(db.run(_tasks_Table.length.result)) { cnt =>
      if (cnt == 0) {
        complete(HttpResponse(StatusCodes.NotFound))
      } else {
        complete(HttpResponse(StatusCodes.OK))
      }
    }
  }
}
