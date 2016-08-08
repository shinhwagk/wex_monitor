package org.gk.services.base

import slick.driver.H2Driver.api._

/**
  * Created by zhangxu on 2016/8/8.
  */
object DatabaseTables {

  case class Task(name: String, category: String, code: String)

  class Tasks(tag: Tag) extends Table[Task](tag, "Tasks") {

    def name = column[String]("NAME", O.PrimaryKey)

    def category = column[String]("CATEGORY")

    def code = column[String]("CODE")

    def * = (name, category, code) <> (Task.tupled, Task.unapply)
  }

}
