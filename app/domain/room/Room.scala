package domain.room

import domain.hotel.Hotel
import domain.reservation.Reservation
import repositories.interfaces.{FromDB, Validated}

case class Room(id: Option[Room.Id], price: Room.Price, hotelId: Hotel.Id){
  //TODO
  def validate: Validated[Room] = Validated(this)

  def addReservations(reservations: List[FromDB[Reservation]]) = RoomWithReservationsDto(this, reservations)
}
case object Room {
  case class Id(value: Long) extends domain.Id
  case class Price(value: Long){
    def >=(that: Price) = this.value >= that.value
    def +(that: Price) = Price(this.value + that.value)
    def -(that: Price) = Price(this.value - that.value)
  }
}



