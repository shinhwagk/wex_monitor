package org.gk.api

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.stream.IOResult
import org.gk.common.MyType._
import org.gk.services.api.MonitorServices
import org.gk.services.base.DatabaseServices
import org.gk.services.base.DatabaseTables.{Task, TaskResult}
import slick.driver.H2Driver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by zhangxu on 2016/8/8.
  */
object MonitorApi {

  val log = Logging(this)

  import DatabaseServices._
  import MonitorServices._

  val route =
    path("api" / "agent" / "monitor" / Segment / Segment) { (task_name, category) =>
      get {
        onSuccess(db.run(_tasks_Table.filter(_.name === task_name).length.result)) { cnt =>
          if (cnt == 0) {
            log.info("没有taask")
            complete(HttpResponse(StatusCodes.NotFound))
          } else {
            log.info("监控")
            db.run(_tasks_Table.filter(_.name === task_name).result.head).foreach { task =>
              MonitorServices.saveFile(task) match {
                case None => MonitorServices.monitorAction(task).foreach(_save_monitor_result(task, _))
                case Some(j) => j.flatMap(_ => MonitorServices.monitorAction(task)).foreach(_save_monitor_result(task, _))
              }
            }
            complete(HttpResponse(StatusCodes.OK))
          }
        }
      } ~
        post {
          entity(as[String]) { data =>
            onSuccess(db.run(_tasks_Table += Task(task_name, category, data))) { cnt =>
              complete(HttpResponse(StatusCodes.OK))
            }
          }
        }
    }
}
