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

  case class Node(ip: String, port: Int, status: String, hostname: String, timestamp: Long, environment: String, retry: Int) {
    def setStatus(new_status: String): Node = Node(ip, port, new_status, hostname, timestamp, environment, retry)

    def updateTimestamp: Node = Node(ip, port, status, hostname, System.currentTimeMillis(), environment, retry)

    def incRetry: Node = Node(ip, port, status, hostname, timestamp, environment, retry + 1)

    def zeroRetry: Node = Node(ip, port, status, hostname, timestamp, environment, 0)

    def calculateLatency: Node = Node(ip, port, status, hostname, System.currentTimeMillis() - timestamp, environment, retry)

  }

  class Nodes(tag: Tag) extends Table[Node](tag, "NODES") {

    def ip = column[String]("IP", O.PrimaryKey)

    def port = column[Int]("PORT")

    def status = column[String]("STATUS")

    def hostname = column[String]("HOSTNAME")

    def timestamp = column[Long]("TIMESTAMP")

    def environment = column[String]("ENVIRONMENT")

    def retry = column[Int]("RETRY")

    def * = (ip, port, status, hostname, timestamp, environment, retry) <> (Node.tupled, Node.unapply)
  }

  case class Task(name: String, category: String)

  class Tasks(tag: Tag) extends Table[Task](tag, "TASKS") {

    def name = column[String]("NAME", O.PrimaryKey)

    def category = column[String]("CATEGORY")

    def * = (name, category) <> (Task.tupled, Task.unapply)
  }


  case class Monitor(node: String, task: String, interval: Int)

  class Monitors(tag: Tag) extends Table[Monitor](tag, "MONITORS") {

    def node = column[String]("NODE")

    def task = column[String]("TASK")

    def interval = column[Int]("INTERVAL")

    def * = (node, task, interval) <> (Monitor.tupled, Monitor.unapply)
  }

}
