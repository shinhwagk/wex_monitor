package org.gk.api

import akka.http.scaladsl.server.Directives._
import org.gk.services.base.DatabaseSerivces
import org.gk.services.base.DatabaseTables.Node

/**
  * Created by zhangxu on 2016/8/8.
  */
object NodeInfo {

  val route = (get & path("api" / "nodes")) {
    import org.gk.services.base.DataTransform._
    onSuccess(DatabaseSerivces.selectNodes) { nodes =>
      complete(nodes.map(_.calculateLatency))
    }
  }
}
