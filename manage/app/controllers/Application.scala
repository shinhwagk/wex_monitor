package controllers

import modles.services.base.{DataTransform, DatabaseServices}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

class Application extends Controller {

  def index = Action { implicit request =>
    Ok
  }

  def nodes = Action.async { implicit request =>
    DatabaseServices.getNodesInfo.map(ls => Ok(DataTransform.nodesDataTransform(ls)))
  }
}