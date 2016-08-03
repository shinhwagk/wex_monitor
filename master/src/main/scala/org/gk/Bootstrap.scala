package org.gk

import org.gk.services.HeartService

/**
  * Created by zhangxu on 2016/8/1.
  */
object Bootstrap {
  import org.gk.services.base.ActorSystemServices._

  def main(args: Array[String]) {
    new HeartService().start.run()
  }

}
