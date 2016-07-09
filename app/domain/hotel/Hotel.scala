package domain.hotel

import domain.room.Room

case class Hotel(id: Option[Hotel.Id], name: Hotel.Name, city: Hotel.City){
  def addRooms(rooms: List[Room]): HotelWithRoomsDto = HotelWithRoomsDto(this, rooms)
}
case object Hotel {
  case class Id(value: Long) extends domain.Id
  case class Name(value: String)
  case class City(value: String)
}


