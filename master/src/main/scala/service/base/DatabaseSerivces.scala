package service.base

import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by zhangxu on 2016/8/1.
  */
object DatabaseSerivces {
  private val db = Database.forConfig("wex_monitor")

  def updateNodeHeartTime(ip: String): Future[Int] = {
    val time: Long = System.currentTimeMillis
    db.run(sqlu"""update nodes set data = json_set(json_set(data,'$$.heart',$time),'$$.status',"normal") where ip = $ip""")
  }

  def updateNodeHeartDie(ip: String): Future[Int] = {
    db.run(sqlu"""update nodes set data = json_set(data,'$$.status',"die") where ip = $ip""")
  }

  def updateDieToRetryNodes: Future[Int] = {
    db.run(sqlu"""update nodes set data = json_set(data,'$$.status',"retry") where JSON_UNQUOTE(data->'$$.status') = 'die'""")
  }

  def getTestHeartNodes: Future[List[String]] = {
    db.run(sql"""select ip from nodes where JSON_UNQUOTE(data->'$$.status') in ('retry','normal')""".as[String]).map(_.toList)
  }

}
