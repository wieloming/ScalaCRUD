package services.hotel

import domain.hotel.{Hotel, HotelForCreateDto, HotelWithRoomsDto}
import domain.reservation.Reservation
import domain.room.{Room, RoomForRegisterDto, RoomWithReservationsDto}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import repositories.interfaces.HotelRepo
import services.reservation.ReservationService
import services.room.RoomService

import scala.concurrent.Future

class HotelService(roomService: RoomService, reservationService: ReservationService, hotelRepository: HotelRepo) {

  def createHotel(hotel: HotelForCreateDto): Future[Hotel.id] = {
    hotelRepository.create(Hotel(None, hotel.name, hotel.city))
  }

  def findById(id: Hotel.id): Future[Option[HotelWithRoomsDto]] = {
    for {
      hotel <- hotelRepository.findById(id)
      withRooms <- getRooms(hotel, id)
    } yield withRooms
  }

  def registerRoom(hotelId: Hotel.id, newRoom: RoomForRegisterDto): Future[Option[Room.id]] = {
    def createRoom(hotel: Option[Hotel]) = hotel match {
      case Some(h) => roomService.create(Room(None, newRoom.price, hotelId)).map(Some(_))
      case _ => Future.successful(None)
    }
    for {
      hotel <- hotelRepository.findById(hotelId)
      roomId <- createRoom(hotel)
    } yield roomId
  }

  def removeRoom(hotelId: Hotel.id, roomId: Room.id): Future[Option[HotelWithRoomsDto]] = {
    for {
      hotel <- hotelRepository.findById(hotelId)
      _ = hotel.map(h => roomService.remove(roomId))
      withRooms <- getRooms(hotel, hotelId)
    } yield withRooms
  }

  def findAvailableRooms(period: Reservation.period, city: String, price: Room.price): Future[List[Room]] = {
    def findReservations(rooms: List[Room]): Future[List[RoomWithReservationsDto]] =
      Future.traverse(rooms) {
        case room@Room(Some(id), _, _) =>
          reservationService.findAllByRoomId(id).map(r => Some(room.addReservations(r)))
        case _ => Future.successful(None)
      }.map(_.flatten)
    def filterBooked(rooms: List[RoomWithReservationsDto]): List[Room] =
      rooms.filter(_.reservations.forall(_.notIn(period))).map(_.room)
    for {
      hotels <- hotelRepository.findAllByCity(city)
      rooms <- roomService.findAllByHotelIds(hotels.flatMap(_.id))
      affordableRooms = rooms.filter(_.price >= price)
      roomsWithReservations <- findReservations(affordableRooms)
      freeRooms = filterBooked(roomsWithReservations)
    } yield freeRooms
  }

  private def getRooms(hotel: Option[Hotel], id: Hotel.id): Future[Option[HotelWithRoomsDto]] =
    hotel match {
      case Some(h) => roomService.findAllByHotelId(id).map(h.addRooms).map(Some(_))
      case _ => Future.successful(None)
    }
}
