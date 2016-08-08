package org.gk.services.base

import slick.driver.H2Driver.api._

/**
  * Created by zhangxu on 2016/8/8.
  */
object DatabaseTables {

  object NodeStatus {
    val DIE = "dir"
    val STOP = "stop"
    val NORMAL = "normal"
    val DOUBT = "doubt"
  }

  case class Node(ip: String, status: String, hostname: String, timestamp: Long, environment: String, retry: Int) {
    def setStatus(new_status: String): Node = Node(ip, new_status, hostname, timestamp, environment, retry)

    def updateTimestamp: Node = Node(ip, status, hostname, System.currentTimeMillis(), environment, retry)

    def incRetry: Node = Node(ip, status, hostname, timestamp, environment, retry + 1)

    def zeroRetry: Node = Node(ip, status, hostname, timestamp, environment, 0)

    def calculateLatency: Node = Node(ip, status, hostname, System.currentTimeMillis() - timestamp, environment, retry)

  }

  class Nodes(tag: Tag) extends Table[Node](tag, "NODES") {

    def ip = column[String]("IP", O.PrimaryKey)

    def status = column[String]("STATUS")

    def hostname = column[String]("HOSTNAME")

    def timestamp = column[Long]("TIMESTAMP")

    def environment = column[String]("ENVIRONMENT")

    def retry = column[Int]("RETRY")

    def * = (ip, status, hostname, timestamp, environment, retry) <> (Node.tupled, Node.unapply)
  }

}
