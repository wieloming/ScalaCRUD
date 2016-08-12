package services.hotel

import domain.hotel.{Hotel, HotelForCreateDto, HotelWithRoomsDto}
import domain.reservation.Reservation
import domain.room.{Room, RoomForRegisterDto, RoomWithReservationsDto}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import repositories.interfaces.{Errors, HotelRepo, Validated}
import services.reservation.ReservationService
import services.room.RoomService
import utils.Futures._
import utils.ValidDataOrErrors

import scala.concurrent.Future

class HotelService(roomService: RoomService, reservationService: ReservationService, hotelRepository: HotelRepo) {

  def createHotel(hotel: HotelForCreateDto): ValidDataOrErrors[Hotel] = {
    hotelRepository.create(hotel.toValidHotel)
  }

  def findById(id: Hotel.Id) = {
    hotelRepository.findById(id).flatMap {
      case Right(Some(Validated(h))) => getRooms(h, id).map(h => Right(Some(h)))
      case Right(None) => Future.successful(Right(None))
      case Left(errors) => Future.successful(Left(errors))
    }
  }

  def registerRoom(hotelId: Hotel.Id, newRoom: RoomForRegisterDto): Future[Option[Room.Id]] = {
    def createRoom(hotel: Option[Hotel]) = hotel match {
      case Some(h) => roomService.create(Room(None, newRoom.price, hotelId)).map(Some(_))
      case _ => Future.successful(None)
    }
    for {
      hotel <- hotelRepository.findById(hotelId)
      roomId <- createRoom(hotel)
    } yield roomId
  }

  def removeRoom(hotelId: Hotel.Id, roomId: Room.Id): Future[Option[HotelWithRoomsDto]] = {
    for {
      hotel <- hotelRepository.findById(hotelId)
      _ = hotel.map(h => roomService.remove(roomId))
      withRooms <- getRooms(hotel, hotelId)
    } yield withRooms
  }

  def findAvailableRooms(period: Reservation.Period, city: Hotel.City, price: Room.Price): Future[List[Room]] = {
    def findReservations(rooms: List[Room]): Future[List[RoomWithReservationsDto]] =
      Future.flatTraverse(rooms) {
        case room@Room(Some(id), _, _) =>
          reservationService.findAllByRoomId(id).map(r => Some(room.addReservations(r)))
        case _ => Future.successful(None)
      }
    def filterBooked(rooms: List[RoomWithReservationsDto]): List[Room] =
      rooms.filter(_.reservations.forall(_.notIn(period))).map(_.room)
    for {
      hotels <- hotelRepository.findAllByCity(city)
      rooms <- roomService.findAllByHotelIds(hotels.flatMap(_.id): _*)
      affordableRooms = rooms.filter(_.price >= price)
      roomsWithReservations <- findReservations(affordableRooms)
      freeRooms = filterBooked(roomsWithReservations)
    } yield freeRooms
  }

  private def getRooms(hotel: Hotel, id: Hotel.Id): Future[HotelWithRoomsDto] =
    roomService.findAllByHotelIds(id).map(hotel.addRooms)
}
