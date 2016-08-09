package org.gk.common

import com.typesafe.config.ConfigFactory

/**
  * Created by zhangxu on 2016/8/9.
  */
object Configure {
  private val conf = ConfigFactory.load();
  val monitor_file_location = conf.getString("wex.monitor.file")
}
