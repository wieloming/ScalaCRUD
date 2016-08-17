package domain.hotel

import domain.room.Room
import repositories.interfaces.{FromDB, Validated}

case class Hotel(id: Option[Hotel.Id], name: Hotel.Name, city: Hotel.City){
  //TODO
  def validate: Validated[Hotel] = Validated(this)

  def addRooms(rooms: List[FromDB[Room]]): HotelWithRoomsDto = HotelWithRoomsDto(this, rooms)
}
case object Hotel {
  case class Id(value: Long) extends domain.Id
  case class Name(value: String)
  case class City(value: String)
}


