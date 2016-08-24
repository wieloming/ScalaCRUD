package domain.room

import domain.Model
import domain.hotel.Hotel
import domain.reservation.Reservation
import repositories.interfaces.{FromDB, Validated}

case class Room(id: Option[Room.ModelId], price: Room.Price, hotelId: Hotel.ModelId) {
  //TODO
  def validate: Validated[Room] = Validated(this)

  def addReservations(reservations: List[FromDB[Reservation, Reservation.ModelId]]) = RoomWithReservationsDto(this, reservations)
}
case object Room extends Model[Room] {
  case class ModelId(value: Long) extends domain.ModelId
  case class Price(value: Long){
    def >=(that: Price) = this.value >= that.value
    def +(that: Price) = Price(this.value + that.value)
    def -(that: Price) = Price(this.value - that.value)
  }
}



