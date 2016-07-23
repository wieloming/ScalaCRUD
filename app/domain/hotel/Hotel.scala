package domain.hotel

import domain.DBid
import domain.room.Room

case class Hotel(name: Hotel.Name, city: Hotel.City) {
  def addRooms(rooms: List[Room]): HotelWithRoomsDto = HotelWithRoomsDto(this, rooms)
  def withId(v: Hotel.Id): Hotel with DBid[Hotel.Id] = {
    new Hotel(name, city) with DBid[Hotel.Id] {
      override val id: Hotel.Id = v
    }
  }
}
case object Hotel {
  case class Id(value: Long) extends domain.Id
  case class Name(value: String)
  case class City(value: String)
}


