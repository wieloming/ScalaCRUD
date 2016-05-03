package controllers

import javax.inject.Inject

import domain.reservation.Reservation
import play.api.mvc._
import mappings.ReservationJson
import services.Container

class BookController @Inject()(container: Container) extends BaseController with ReservationJson {

  def book = Action.async(parse.json[Reservation]) { request =>
    container.bookService.book(request.body)
  }
}
