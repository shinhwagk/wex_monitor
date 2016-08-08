package org.gk.services.base

import org.gk.common.MyType._
import org.gk.services.base.DatabaseTables.{Node, Nodes}
import slick.backend.DatabasePublisher
import slick.driver.MySQLDriver.api._
import slick.profile.SqlAction

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

  private val log = Logging(this)

  import DataTransform._
  import spray.json._

  db.run(_nodes_Table.schema.create).onComplete {
    case Success(_) =>
      Source.fromFile("master/data/nodes.json")
        .mkString
        .parseJson
        .convertTo[List[Node]]
        .foreach { node =>
          insertNode(node).onComplete {
            case Success(cnt) => log.info(s"Database Init load node: ${node}")
            case Failure(ex) => log.info(s"Database Init failure: ${ex.getMessage}")
          }
        }
    case Failure(ex) => log.info(s"Database Init failure: ${ex.getMessage}")
  }

  def insertNode(node: Node): Future[Int] = {
    db.run(_nodes_Table += node)
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

  //  def updateNodeHeartTimestamp(ip: String, timestamp: Long): Future[Int] = {
  //    db.run(sqlu"""UPDATE nodes SET data = JSON_SET(data,'$$.timestamp',$timestamp) WHERE ip = $ip""")
  //  }
  //
  //  def updateNodeStatusToDie(ip: String): Future[Int] = {
  //    db.run(sqlu"""UPDATE nodes SET data = JSON_SET(data,'$$.status',"die") WHERE ip = $ip""")
  //  }
  //
  //  def updateNodeStatusToNormal(ip: String): Future[Int] = {
  //    db.run(sqlu"""UPDATE nodes SET data = JSON_SET(data,'$$.status',"normal") WHERE ip = $ip and data->'$$.status' in ('die','doubt')""")
  //  }
  //
  //  def updateNodeStatusToDoubt: Future[Int] = {
  //    db.run(sqlu"""UPDATE nodes SET data = JSON_SET(data,'$$.status',"doubt") WHERE JSON_UNQUOTE(data->'$$.status') = 'die'""")
  //  }
  //
  //  def updateNodeHeartRetryZero(ip: String) = {
  //    db.run(sqlu"""UPDATE nodes SET data = JSON_SET(data,'$$.heart.retry',0) WHERE ip = $ip""")
  //  }
  //
  //  def selectHeartNodes(timestamp: Long): Future[Vector[String]] = {
  //    db.run(sql"""SELECT JSON_OBJECT('ip',data -> '$$.ip','retry',data->'$$.heart.retry') FROM nodes WHERE JSON_UNQUOTE(data -> '$$.status') IN ('doubt','normal') or (JSON_UNQUOTE(data -> '$$.status') = 'die' AND data->'$$.timestamp' < $timestamp)""".as[String])
  //  }
  //
  //  def updateNodeHeartRetryInc(ip: String): Future[Int] = {
  //    db.run(sqlu"""UPDATE nodes SET data = JSON_SET(data,'$$.heart.retry',data->'$$.heart.retry' + 1) WHERE ip = $ip""")
  //  }
  //
  //  def dml(sqlu: SqlAction[Int, NoStream, Effect]) = {
  //    db.run(sqlu)
  //  }
}
