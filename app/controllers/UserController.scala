package controllers

import javax.inject.Singleton

import javax.inject._
import play.api.mvc._
import domain.user.User
import mappings.{ReservationJson, UserJson}
import services.Container

@Singleton
class UserController @Inject()(container: Container) extends BaseController with UserJson with ReservationJson {

  def createUser() = Action.async(parse.json[User.ForCreate]) { request =>
    container.userService.createUser(request.body)
  }

  def findUserById(id: Long) = Action.async {
    container.userService.findById(User.ModelId(id))
  }

  def findReservations(id: Long) = Action.async {
    container.userService.findReservations(User.ModelId(id))
  }
}
