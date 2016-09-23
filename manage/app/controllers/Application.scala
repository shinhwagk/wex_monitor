package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import akka.stream.Materializer
import modles.services.base.WebSocketServices.NodesWebSocketActor
import play.api.libs.streams.ActorFlow
import play.api.libs.ws.WSClient
import play.api.mvc._

class Application @Inject()(implicit system: ActorSystem, materializer: Materializer, ws: WSClient) extends Controller {

  def index = Action { implicit request =>
    Ok
  }

  def nodes = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef(out => NodesWebSocketActor.props(out,ws))
  }
}