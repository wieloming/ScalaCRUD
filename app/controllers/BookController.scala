package controllers

import javax.inject.Inject

import domain.reservation.Reservation
import play.api.mvc._
import mappings.{ReservationJson, RoomJson}
import play.api.libs.json.Json
import services.Container
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class BookController @Inject()(container: Container) extends Controller with ReservationJson {

  def book = Action.async(parse.json[Reservation]) { request =>
    container.bookService.book(request.body).map { reservationId =>
      if (reservationId.nonEmpty) Ok(Json.toJson(reservationId))
      else NotFound
    }
  }
}
