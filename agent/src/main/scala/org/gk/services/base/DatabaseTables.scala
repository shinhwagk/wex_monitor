package org.gk.services.base

import slick.driver.H2Driver.api._

/**
  * Created by zhangxu on 2016/8/8.
  */
object DatabaseTables {

  case class Task(name: String, category: String, code: String)

  class Tasks(tag: Tag) extends Table[Task](tag, "TASKS") {

    def name = column[String]("NAME", O.PrimaryKey)

    def category = column[String]("CATEGORY")

    def code = column[String]("CODE")

    def * = (name, category, code) <> (Task.tupled, Task.unapply)
  }


  case class TaskResult(name: String, result: String, timestamp: Long)

  class TaskResults(tag: Tag) extends Table[TaskResult](tag, "TASKRESULTS") {

    def name = column[String]("NAME", O.PrimaryKey)

    def result = column[String]("RESULT")

    def timestamp = column[Long]("TIMESTAMP")

    def * = (name, result, timestamp) <> (TaskResult.tupled, TaskResult.unapply)
  }

}
