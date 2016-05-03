package controllers

import javax.inject.Inject

import domain.hotel.{Hotel, HotelForCreateDto}
import domain.reservation.Reservation
import domain.room.{Room, RoomForRegisterDto}
import play.api.mvc._
import mappings.RoomJson
import org.joda.time.LocalDate
import services.Container

class HotelController @Inject()(container: Container) extends BaseController with RoomJson {

  def createHotel() = Action.async(parse.json[HotelForCreateDto]) { request =>
    container.hotelService.createHotel(request.body)
  }

  def findHotelById(id: Long) = Action.async {
    container.hotelService.findById(Hotel.id(id))
  }

  def registerRoom(id: Long) = Action.async(parse.json[RoomForRegisterDto]) { request =>
    container.hotelService.registerRoom(Hotel.id(id), request.body)
  }

  def removeRoom(hotelId: Long, roomId: Long) = Action.async {
    container.hotelService.removeRoom(Hotel.id(hotelId), Room.id(roomId))
  }

  def findAvailableRooms(from: LocalDate, to: LocalDate, city: String, price: Long) = Action.async { request =>
    container.hotelService.findAvailableRooms(Reservation.period(from, to), city, price)
  }
}
