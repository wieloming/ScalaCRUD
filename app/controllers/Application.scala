package controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action {response =>
    Ok(views.html.main("Potato!"))
  }
}