package services.hotel

import domain.hotel.{Hotel, HotelForCreateDto}
import domain.reservation.Reservation
import domain.room.{Room, RoomForRegisterDto, RoomWithReservationsDto}
import repositories.interfaces.{FromDB, HotelRepo}
import services.reservation.ReservationService
import services.room.RoomService
import utils.ValueOrErrors

class HotelService(roomService: RoomService, reservationService: ReservationService, hotelRepository: HotelRepo) {

  def createHotel(hotel: HotelForCreateDto): ValueOrErrors[Hotel.ModelId] = {
    hotelRepository.create(hotel.toValidHotel)
  }

  def findById(id: Hotel.ModelId): ValueOrErrors[FromDB[Hotel, Hotel.ModelId]] = {
    hotelRepository.findById(id)
  }

  def registerRoom(hotelId: Hotel.ModelId, newRoom: RoomForRegisterDto): ValueOrErrors[Room.ModelId] = {
     for {
      hotel <- hotelRepository.findById(hotelId)
      room <- roomService.create(Room(None, newRoom.price, hotelId))
    } yield room
  }

  def findAvailableRooms(period: Reservation.Period, city: Hotel.City, price: Room.Price): ValueOrErrors[List[Room]] = {
    def filterBooked(rooms: List[RoomWithReservationsDto]): List[Room] =
      rooms.filter(_.reservations.forall(_.notIn(period))).map(_.room)
    for {
      hotels <- hotelRepository.findAllByCity(city)
      rooms <- roomService.findAllByHotelIds(hotels.map(_.id): _*)
      affordableRooms = rooms.filter(_.price >= price)
      roomsWithReservations <- ValueOrErrors.traverse(rooms)(r => reservationService.findAllByRoomId(r.id).map(r.addReservations))
      freeRooms = filterBooked(roomsWithReservations)
    } yield freeRooms
  }
}