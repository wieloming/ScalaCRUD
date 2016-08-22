package domain.hotel

import domain.WithId
import domain.room.Room
import repositories.interfaces.Validated

case class Hotel(id: Option[Hotel.Id], name: Hotel.Name, city: Hotel.City) extends WithId[Hotel.Id] {
  //TODO
  def validate: Validated[Hotel] = Validated(this)

  def addRooms(rooms: List[Room]): HotelWithRoomsDto = HotelWithRoomsDto(this, rooms)
}
case object Hotel {
  case class Id(value: Long) extends domain.Id
  case class Name(value: String)
  case class City(value: String)
}


