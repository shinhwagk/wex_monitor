package common

import akka.event.LogSource

/**
  * Created by zhangxu on 2016/8/2.
  */
object MyType {
  implicit val logSource: LogSource[AnyRef] = new LogSource[AnyRef] {
    def genString(o: AnyRef): String = o.getClass.getName

    override def getClazz(o: AnyRef): Class[_] = o.getClass
  }
}
