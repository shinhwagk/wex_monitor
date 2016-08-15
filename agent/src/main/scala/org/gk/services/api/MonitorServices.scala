package org.gk.services.api

import java.io.File

import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.ByteString
import org.gk.common.MyType._
import org.gk.services.base.DatabaseTables.{Task, TaskResult}
import slick.driver.H2Driver.api._

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by zhangxu on 2016/8/9.
  */
object MonitorServices {

  import org.gk.common.Configure._
  import org.gk.services.base.ActorSystemServices._
  import org.gk.services.base.DatabaseServices._

  val log = Logging(this)

  def saveFile(task: Task): Option[Future[IOResult]] = {
    val outFile = new File(s"${monitor_file_location}/${task.name}.${task.category}")
    if (outFile.exists()) {
      None
    } else {
      Some(Source.single(task.code).map(s => ByteString(s + "\n")).runWith(FileIO.toPath(outFile.toPath)))
    }
  }

  def monitorAction(task: Task): Future[String] = Future {
    task.category match {
      case "py" => monitorActionChoice(monitorPythonAction, task)
      case "sql" => monitorActionChoice(monitorSqlAction, task)
    }
  }

  def monitorActionChoice(f: Task => String, task: Task): String = {
    f(task)
  }

  def monitorPythonAction(task: Task): String = {
    import scala.sys.process._
    val b = s"python ${monitor_file_location}/${task.name}.${task.category}"
    try {
      println(b.!!)
    } catch {
      case ex: Exception => println(ex.getMessage)
    }
    println(b)
    b
  }

  def monitorSqlAction(task: Task): String = {
    "aaa"
  }

  val _save_monitor_result = (task: Task, result: String) => {
    db.run(_task_Result_Table.filter(_.name === task.name).update(TaskResult(task.name, result, System.currentTimeMillis()))).onComplete {
      case Success(_) => log.info("save succes")
      case Failure(ex) => log.info("save failure")
    }
  }


}
