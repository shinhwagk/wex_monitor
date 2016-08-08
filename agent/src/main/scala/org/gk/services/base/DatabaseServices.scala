package org.gk.services.base

import org.gk.common.MyType._
import org.gk.services.base.DatabaseTables.{Task, Tasks}
import slick.driver.H2Driver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * Created by zhangxu on 2016/8/8.
  */
object DatabaseServices {
  private val db = Database.forConfig("agent")

  private val _tasks_Table = TableQuery[Tasks]

  private val log = Logging(this)

  db.run(_tasks_Table.schema.create).onComplete {
    case Success(_) => log.info("Database Init success")
    case Failure(ex) => log.info(s"Database Init failure: ${ex.getMessage}")
  }

  def _insert_task(task: Task) = {
    db.run(_tasks_Table += task)
  }

  def _count_task(task_name: String) = {
    db.run(_tasks_Table.filter(_.name === task_name).length.result)
  }
}
