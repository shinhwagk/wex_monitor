package org.gk.services.base

import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by zhangxu on 2016/8/1.
  */
object DatabaseSerivces {
  private val db = Database.forConfig("wex_monitor")

//  implicit def vectorToList[T](v: Future[Vector[T]]): Future[List[T]] = v.map(_.toList)

  def updateNodeHeartTimestamp(ip: String): Future[Int] = {
    val time: Long = System.currentTimeMillis
    db.run(sqlu"""UPDATE nodes SET data = JSON_SET(JSON_SET(data,'$$.heart.timestamp',$time),'$$.status',"normal") WHERE ip = $ip""")
  }

  def updateNodeStatusToDie(ip: String): Future[Int] = {
    db.run(sqlu"""UPDATE nodes SET data = JSON_SET(data,'$$.status',"die") WHERE ip = $ip""")
  }

  def updateNodeStatusToDoubt: Future[Int] = {
    db.run(sqlu"""UPDATE nodes SET data = JSON_SET(data,'$$.status',"doubt") WHERE JSON_UNQUOTE(data->'$$.status') = 'die'""")
  }

  def updateNodeHeartRetryZero(ip:String) ={
    db.run(sqlu"""UPDATE nodes SET data = JSON_SET(data,'$$.heart.retry',0) WHERE ip = $ip""")
  }

  def selectCheckHeartNodes: Future[List[String]] = {
    db.run(sql"""SELECT ip FROM nodes WHERE JSON_UNQUOTE(data->'$$.status') IN ('doubt','normal')""".as[String]).map(_.toList)
  }

  def updateNodeHeartRetryInc(ip:String): Future[Int] = {
    db.run(sqlu"""UPDATE nodes SET data = JSON_SET(data,'$$.heart.retry',data->'$$.heart.retry' + 1) WHERE ip = $ip""")
  }

  def selectNodeHeartRetryCount(ip:String): Future[Int] = {
    db.run(sql"""SELECT data->'$$.heart.retry' FROM nodes WHERE ip = $ip""".as[Int].head)
  }

}
