package controllers

import domain.reservation.Reservation
import play.api.mvc._
import mappings.RoomJson
import play.api.libs.json.Json
import services.ServicesComponent
import play.api.libs.concurrent.Execution.Implicits.defaultContext

trait BookController extends Controller with RoomJson {
  self: ServicesComponent =>

  def book = Action.async(parse.json[Reservation]) { request =>
    bookService.book(request.body).map { reservationId =>
      if (reservationId.nonEmpty) Ok(Json.toJson(reservationId))
      else NotFound
    }
  }
}

object BookAPI extends BookController with ServicesComponent
