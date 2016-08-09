package org.gk.services.base

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.gk.services.base.DatabaseTables.{Monitor, Node, Task}
import spray.json.DefaultJsonProtocol

/**
  * Created by zhangxu on 2016/8/4.
  */
object DataTransform extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val _node_Format = jsonFormat7(Node)
  implicit val _task_Format = jsonFormat2(Task)
  implicit val _monitor_Format = jsonFormat3(Monitor)
}
