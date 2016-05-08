package domain.room

import domain.Id
import domain.hotel.Hotel
import domain.reservation.Reservation

case class Room(id: Option[Room.id], price: Long, hotelId: Hotel.id){
  def addReservations(reservations: List[Reservation]) = RoomWithReservationsDto(this, reservations)
}
case object Room {
  case class id(value: Long) extends Id
}



