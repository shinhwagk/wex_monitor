package service.base

import play.api.libs.ws.WSResponse
import scala.concurrent.Future

/**
  * Created by zhangxu on 2016/8/1.
  */
object ClientServices {
import service.base.ActorSystemServices._
  def heartService(ip: String): Future[WSResponse] = {
    val url = s"http://${ip}:8080/api/heart"
    wsClient.url(url).get()
  }
}