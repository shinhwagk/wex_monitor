package org.gk.common

import com.typesafe.config.ConfigFactory

/**
  * Created by zhangxu on 2016/8/3.
  */
object Configure {
  private val conf = ConfigFactory.load();
  val die_retry_interval = conf.getInt("wex.heart.die_retry_interval")
  val heart_interval = conf.getInt("wex.heart.interval")
  val heart_retry = conf.getInt("wex.heart.retry")


  val monitor_interval = conf.getInt("wex.monitor.interval")
}
