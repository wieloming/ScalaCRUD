package domain.hotel

import domain.room.Room

case class HotelWithRoomsDto(hotel: Hotel, rooms: List[Room])
