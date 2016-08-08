package modles.common

import com.typesafe.config.ConfigFactory

/**
  * Created by zhangxu on 2016/8/3.
  */
object Configure {
  private val conf = ConfigFactory.load();
  val _master_ip = conf.getString("wex_monitor.master.ip")
  val _master_post = conf.getInt("wex_monitor.master.post")
}
