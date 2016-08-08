package org.gk.services.base

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.gk.services.base.DatabaseTables.Node
import spray.json.DefaultJsonProtocol

/**
  * Created by zhangxu on 2016/8/4.
  */
object DataTransform extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val _node_Format = jsonFormat6(Node)
}
