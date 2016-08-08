package org.gk.services.base

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.gk.services.base.DatabaseTables.Task
import spray.json.DefaultJsonProtocol

/**
  * Created by zhangxu on 2016/8/4.
  */
object DataTransform extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val _task_Format = jsonFormat3(Task.apply)
}
