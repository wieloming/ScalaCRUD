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
class UserController @Inject()(container: Container) extends BaseController with UserJson with ReservationJson {

  def createUser() = Action.async(parse.json[UserForCreateDto]) { request =>
    container.userService.createUser(request.body)
  }

  def findUserById(id: Long) = Action.async {
    container.userService.findById(User.id(id))
  }

  def findReservations(id: Long) = Action.async {
    container.userService.findReservations(User.id(id))
  }
}
