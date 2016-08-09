package org.gk.common

import org.apache.logging.log4j.{LogManager, Logger}

/**
  * Created by zhangxu on 2016/8/2.
  */
object MyType {

  implicit def Logging(o: Any):Logger = {
    LogManager.getLogger(o.getClass.getName);
  }
}
