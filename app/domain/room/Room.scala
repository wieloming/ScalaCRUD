package domain.room

import domain.Id
import domain.hotel.Hotel
import domain.reservation.Reservation

case class Room(id: Option[Room.id], price: Room.price, hotelId: Hotel.id){
  def addReservations(reservations: List[Reservation]) = RoomWithReservationsDto(this, reservations)
}
case object Room {
  case class id(value: Long) extends Id
  case class price(value: Long){
    def >=(that: price) = this.value >= that.value
    def +(that: price) = price(this.value + that.value)
    def -(that: price) = price(this.value - that.value)
  }
}



