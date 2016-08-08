package org.gk.api

import akka.http.scaladsl.server.Directives._
import org.gk.services.base.DatabaseSerivces

/**
  * Created by zhangxu on 2016/8/8.
  */
object NodeInfo {

  val route = (get & path("api" / "master" / "nodes")) {
    import org.gk.services.base.DataTransform._
    onSuccess(DatabaseSerivces.selectNodes) { nodes =>
      complete(nodes.map(_.calculateLatency))
    }

  }
}
