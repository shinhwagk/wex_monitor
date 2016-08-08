package org.gk.services

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.gk.common.Configure
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by zhangxu on 2016/8/8.
  */
object MonitorService {
  val _monitor_interval = Configure.monitor_interval * 1000

}

class MonitorService(implicit system: ActorSystem, materializer: ActorMaterializer) {

  import MonitorService._

  val start = {
    Future {
      while (true) {
        Thread.sleep(_monitor_interval)
      }
    }
  }
}