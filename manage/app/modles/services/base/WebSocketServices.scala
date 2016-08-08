package modles.services.base

import akka.actor.{Actor, ActorRef, Props}
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.duration._
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by zhangxu on 2016/8/4.
  */
object WebSocketServices {

  object NodesWebSocketActor {
    def props(out: ActorRef, ws: WSClient): Props = Props(new NodesWebSocketActor(out, ws))
  }

  class NodesWebSocketActor(out: ActorRef, ws: WSClient) extends Actor {
    context.system.scheduler.schedule(Duration.Zero, 1 second, self, 0)

    import modles.common.Configure._

    def receive = {

      case d: Int =>
        val url = s"""http://${_master_ip}:${_master_post}/api/master/nodes"""
        ws.url(url).get().onSuccess {
          case wr => {
            out ! wr.body
          }
        }
    }
  }

}
