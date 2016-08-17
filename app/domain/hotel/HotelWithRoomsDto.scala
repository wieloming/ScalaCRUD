package domain.hotel

import domain.room.Room
import repositories.interfaces.FromDB$

case class HotelWithRoomsDto(hotel: Hotel, rooms: List[FromDB[Room]])
