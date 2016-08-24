package domain.hotel

import domain.Model
import domain.room.Room
import repositories.interfaces.Validated

case class Hotel(id: Option[Hotel.ModelId], name: Hotel.Name, city: Hotel.City) {
  //TODO
  def validate: Validated[Hotel] = Validated(this)

  def addRooms(rooms: List[Room]): HotelWithRoomsDto = HotelWithRoomsDto(this, rooms)
}
case object Hotel extends Model[Hotel] {
  type dupa = this.type
  case class ModelId(value: Long) extends domain.ModelId
  case class Name(value: String)
  case class City(value: String)
}


