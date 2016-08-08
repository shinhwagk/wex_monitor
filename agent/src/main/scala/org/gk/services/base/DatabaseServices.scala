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
  val db = Database.forConfig("agent")

  val _tasks_Table = TableQuery[Tasks]

  private val log = Logging(this)

  db.run(_tasks_Table.schema.create).onComplete {
    case Success(_) => log.info("Database Init success")
    case Failure(ex) => log.info(s"Database Init failure: ${ex.getMessage}")
  }

}
