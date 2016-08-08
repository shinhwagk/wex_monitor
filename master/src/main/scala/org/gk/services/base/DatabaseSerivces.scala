package org.gk.services.base

import org.gk.common.MyType._
import org.gk.services.base.DatabaseTables._
import slick.backend.DatabasePublisher
import slick.dbio.Effect.Schema
import slick.driver.MySQLDriver.api._
import slick.lifted.AbstractTable
import slick.profile.FixedSqlAction
import spray.json.RootJsonFormat

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source
import scala.util.{Failure, Success}

/**
  * Created by zhangxu on 2016/8/1.
  */
object DatabaseSerivces {

  val db = Database.forConfig("master")

  val _nodes_Table = TableQuery[Nodes]

  val _tasks_Table = TableQuery[Tasks]

  val _monitor_Table = TableQuery[Monitors]

  private val log = Logging(this)

  import spray.json._

  db.run(_nodes_Table.schema.create).onComplete {
    case Success(_) =>
      Source.fromFile("master/data/nodes.json")
        .mkString
        .parseJson
        .convertTo[List[Node]]
        .foreach { node =>
          db.run(_nodes_Table += node).onComplete {
            case Success(cnt) => log.info(s"Database Init load node: ${node}")
            case Failure(ex) => log.info(s"Database Init failure: ${ex.getMessage}")
          }
        }
    case Failure(ex) => log.info(s"Database Init failure: ${ex.getMessage}")
  }

  db.run(_tasks_Table.schema.create).onComplete {
    case Success(_) =>
      Source.fromFile("master/data/tasks.json")
        .mkString
        .parseJson
        .convertTo[List[Task]]
        .foreach { task =>
          db.run(_tasks_Table += task).onComplete {
            case Success(cnt) => log.info(s"Database Init load node: ${task}")
            case Failure(ex) => log.info(s"Database Init failure: ${ex.getMessage}")
          }
        }
    case Failure(ex) => log.info(s"Database Init failure: ${ex.getMessage}")
  }

  db.run(_tasks_Table.schema.create).onComplete {
    case Success(_) =>
      Source.fromFile("master/data/monitor.json")
        .mkString
        .parseJson
        .convertTo[List[Monitor]]
        .foreach { monitor =>
          db.run(_tasks_Table += monitor).onComplete {
            case Success(cnt) => log.info(s"Database Init load node: ${monitor}")
            case Failure(ex) => log.info(s"Database Init failure: ${ex.getMessage}")
          }
        }
    case Failure(ex) => log.info(s"Database Init failure: ${ex.getMessage}")
  }


  //  def selectNodes: Future[Seq[Node]] = {
  //    db.run(_nodes_Table.result)
  //  }
  //
  //  def selectNodesByStream: DatabasePublisher[Node] = {
  //    db.stream(_nodes_Table.result)
  //  }
  //
  //
  //  def updateNode(node: Node) = {
  //    db.run(_nodes_Table.filter(_.ip === node.ip).update(node))
  //  }

}
