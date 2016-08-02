package service.base

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.ahc.AhcWSClient

/**
  * Created by zhangxu on 2016/8/2.
  */
object ActorSystemServices {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val wsClient: AhcWSClient = AhcWSClient()
}
