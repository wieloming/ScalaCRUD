package domain.hotel

import domain.room.Room

case class Hotel(id: Option[Hotel.id], name: String, city: String){
  def addRooms(rooms: List[Room]): HotelWithRoomsDto = HotelWithRoomsDto(this, rooms)
}
case object Hotel {
  case class id(value: Long) extends AnyVal
}
case class HotelForCreateDto(name: String, city: String)
case class HotelWithRoomsDto(hotel: Hotel, rooms: List[Room])
