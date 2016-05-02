package mappings

import domain.hotel.HotelWithRoomsDto
import domain.reservation.Reservation
import domain.room.{Room, RoomForRegisterDto}
import play.api.libs.json._

trait RoomJson extends UserJson with HotelJson{
  implicit val RoomForRegisterDtoFormat = Json.format[RoomForRegisterDto]
  implicit val RoomIdFormat = Json.format[Room.id]
  implicit val RoomFormat = Json.format[Room]
  implicit val hotelWithRoomsFormat = Json.format[HotelWithRoomsDto]
}
