package org.gk.api

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives._
import org.gk.services.base.DatabaseServices
import org.gk.services.base.DatabaseTables.Task
import slick.driver.H2Driver.api._

/**
  * Created by zhangxu on 2016/8/8.
  */
object TaskApi {

  import org.gk.services.base.DataTransform._
  import DatabaseServices._

  val route =
    path("api" / "agent" / "task") {
      post {
        entity(as[Task]) { task: Task =>
          onSuccess(db.run(_tasks_Table += task)) { none =>
            complete(HttpResponse())
          }
        }
      } ~
        put {
          entity(as[Task]) { task: Task =>
            onSuccess(db.run(_tasks_Table += task)) { none =>
              complete(HttpResponse())
            }
          }
        }
    }
}
