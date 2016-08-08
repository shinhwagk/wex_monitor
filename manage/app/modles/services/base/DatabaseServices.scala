package modles.services.base

import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by zhangxu on 2016/8/3.
  */
object DatabaseServices {
//  private val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
//  private val db = dbConfig.db
//  import dbConfig.driver.api._

  private val db = Database.forConfig("wex_monitor")

  def getNodesInfo: Future[List[String]] = {
    db.run(sql"""SELECT JSON_REMOVE(data,'$$.heart') from nodes""".as[String]).map(_.toList)
  }

}
