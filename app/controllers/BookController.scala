package controllers

import javax.inject.Singleton

import com.google.inject.Inject
import domain.reservation.Reservation
import play.api.mvc._
import mappings.RoomJson
import play.api.libs.json.Json
import services.Container
import play.api.libs.concurrent.Execution.Implicits.defaultContext

@Singleton
class BookController @Inject()(container: Container) extends Controller with RoomJson {
  self: Container =>

  def book = Action.async(parse.json[Reservation]) { request =>
    bookService.book(request.body).map { reservationId =>
      if (reservationId.nonEmpty) Ok(Json.toJson(reservationId))
      else NotFound
    }
  }
}
