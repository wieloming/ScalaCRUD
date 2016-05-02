package controllers

import javax.inject.Singleton

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import domain.user.{User, UserForCreateDto}
import mappings.{ReservationJson, UserJson}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.Container

@Singleton
class UserController @Inject()(container: Container) extends Controller with UserJson with ReservationJson {

  def createUser() = Action.async(parse.json[UserForCreateDto]) { request =>
    container.userService.createUser(request.body).map(response => Ok(Json.toJson(response)))
  }

  def findUserById(id: Long) = Action.async {
    container.userService.findById(User.id(id)).map { user =>
      if (user.isDefined) Ok(Json.toJson(user))
      else NotFound
    }
  }

  def findReservations(id: Long) = Action.async {
    container.userService.findReservations(User.id(id)).map(response => Ok(Json.toJson(response)))
  }
}
