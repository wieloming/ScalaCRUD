package controllers

import domain.hotel.{Hotel, HotelForCreateDto}
import domain.room.{Room, RoomForRegisterDto}
import play.api.mvc._
import mappings.RoomJson
import org.joda.time.LocalDate
import play.api.libs.json.Json
import services.ServicesComponent
import play.api.libs.concurrent.Execution.Implicits.defaultContext

trait HotelController extends Controller with RoomJson {
  self: ServicesComponent =>

  def createHotel() = Action.async(parse.json[HotelForCreateDto]) { request =>
    hotelService.createHotel(request.body).map(response => Ok(Json.toJson(response)))
  }

  def findHotelById(id: Long) = Action.async {
    hotelService.findById(Hotel.id(id)).map { hotel =>
      if (hotel.isDefined) Ok(Json.toJson(hotel))
      else NotFound
    }
  }

  def registerRoom(id: Long) = Action.async(parse.json[RoomForRegisterDto]) { request =>
    hotelService.registerRoom(Hotel.id(id), request.body).map { roomId =>
      if (roomId.isDefined) Ok(Json.toJson(roomId))
      else NotFound
    }
  }

  def removeRoom(hotelId: Long, roomId: Long) = Action.async {
    hotelService.removeRoom(Hotel.id(hotelId), Room.id(roomId)).map { hotel =>
      if (hotel.isDefined) Ok(Json.toJson(hotel))
      else NotFound
    }
  }

  def findAvailableRooms(from: LocalDate, to: LocalDate, city: String, price: Long) = Action.async { request =>
    hotelService.findAvailableRooms(from, to, city, price).map{ rooms =>
      if (rooms.nonEmpty) Ok(Json.toJson(rooms))
      else NotFound
    }
  }
}

object HotelAPI extends HotelController with ServicesComponent
