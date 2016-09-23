package org.gk.services.base


import org.gk.services.base.DatabaseTables._
import slick.backend.DatabasePublisher
import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source
import scala.util.{Failure, Success}


/**
  * Created by zhangxu on 2016/8/1.
  */
object DatabaseSerivces {

  import org.gk.common.MyType._

  private val log = Logging(this)

  import DataTransform._
  import spray.json._

  val db = Database.forConfig("master")

  val _nodes_Table = TableQuery[Nodes]

  val _tasks_Table = TableQuery[Tasks]

  val _monitor_Table = TableQuery[Monitors]

  val _load_text_to_json = (file: String) => Source.fromFile(file).mkString.parseJson

  val _init_data = DBIO.seq(
    (_nodes_Table.schema ++ _tasks_Table.schema ++ _monitor_Table.schema).create,
    _nodes_Table ++= _load_text_to_json("master/data/nodes.json").convertTo[List[Node]],
    _tasks_Table ++= _load_text_to_json("master/data/tasks.json").convertTo[List[Task]]
//    ,
//    _monitor_Table ++= _load_text_to_json("master/data/monitor.json").convertTo[List[Monitor]]
  )

  def initDatabase: Future[Unit] = {
    db.run(_init_data)
  }

  def selectNodes: Future[Seq[Node]] = {
    db.run(_nodes_Table.result)
  }

  def selectNodesByStream: DatabasePublisher[Node] = {
    db.stream(_nodes_Table.result)
  }

  def updateNode(node: Node) = {
    db.run(_nodes_Table.filter(_.ip === node.ip).update(node))
  }



}




