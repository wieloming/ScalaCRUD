package controllers

import com.google.inject.Inject
import play.api.mvc._
import play.api.libs.json._
import domain.user.{User, UserForCreateDto}
import mappings.{ReservationJson, UserJson}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.Container

class UserController @Inject()(container: Container) extends Controller with UserJson with ReservationJson {
  self: Container =>

  def createUser() = Action.async(parse.json[UserForCreateDto]) { request =>
    userService.createUser(request.body).map(response => Ok(Json.toJson(response)))
  }

  def findUserById(id: Long) = Action.async {
    userService.findById(User.id(id)).map { user =>
      if (user.isDefined) Ok(Json.toJson(user))
      else NotFound
    }
  }

  def findReservations(id: Long) = Action.async {
    userService.findReservations(User.id(id)).map(response => Ok(Json.toJson(response)))
  }
}
