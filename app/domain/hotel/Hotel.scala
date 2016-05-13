package domain.hotel

import domain.Id
import domain.room.Room

case class Hotel(id: Option[Hotel.id], name: Hotel.name, city: String){
  def addRooms(rooms: List[Room]): HotelWithRoomsDto = HotelWithRoomsDto(this, rooms)
}
case object Hotel {
  case class id(value: Long) extends Id
  case class name(value: String)
}


