package org.gk.services.base

import org.gk.common.MyType._
import org.gk.services.base.DatabaseTables._
import slick.driver.H2Driver.api._

import scala.concurrent.Future

/**
  * Created by zhangxu on 2016/8/8.
  */
object DatabaseServices {
  val db = Database.forConfig("agent")

  val _tasks_Table = TableQuery[Tasks]

  val _task_Result_Table = TableQuery[TaskResults]

  private val log = Logging(this)

  val _init_table = DBIO.seq(
    (_tasks_Table.schema ++ _task_Result_Table.schema).create
  )

  def initDatabase: Future[Unit] = {
    db.run(_init_table)
  }

}
