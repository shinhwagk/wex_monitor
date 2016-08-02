import service.HeartService

/**
  * Created by zhangxu on 2016/8/1.
  */
object Bootstrap {

  import service.base.ActorSystemServices._

  def main(args: Array[String]) {
    new HeartService().start.run()
  }

}
