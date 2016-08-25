package domain.room

import domain.{ModelForCreate, WithId, Model}
import domain.hotel.Hotel
import domain.reservation.Reservation
import repositories.interfaces.Validated

case class Room(id: Room.ModelId, price: Room.Price, hotelId: Hotel.ModelId) extends WithId[Room] {
  def addReservations(reservations: List[Reservation]) = RoomWithReservationsDto(this, reservations)
}
case object Room extends Model[Room] {
  case class ForCreate(price: Price, hotelId: Hotel.ModelId) extends ModelForCreate[Room, Room.ModelId] {
    //TODO
    def validate: Validated[Room.ForCreate] = Validated(this)
    override def toModel(id: Room.ModelId): Room = Room(id, price, hotelId)
  }
  case class ModelId(value: Long) extends domain.ModelId[Room]
  case class Price(value: Long){
    def >=(that: Price) = this.value >= that.value
    def +(that: Price) = Price(this.value + that.value)
    def -(that: Price) = Price(this.value - that.value)
  }
}



