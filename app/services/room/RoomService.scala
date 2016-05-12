package services.room

import domain.hotel.Hotel
import domain.reservation.Reservation
import domain.room.Room
import domain.room.Room.id
import services.reservation.ReservationService
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import repositories.interfaces.RoomRepo

import scala.concurrent.Future

class RoomService(reservationService: ReservationService, roomRepository: RoomRepo) {

  def remove(id: Room.id): Future[Boolean] = roomRepository.remove(id)

  def create(room: Room): Future[id] = roomRepository.create(room: Room)

  def findByIds(ids: List[Room.id]): Future[List[Room]] = roomRepository.findByIds(ids)

  def findById(id: Room.id): Future[Option[Room]] = roomRepository.findById(id)

  def isFreeBetween(roomId: Room.id, period: Reservation.period): Future[Option[Boolean]] = {
    def findReservations(room: Option[Room]): Future[List[Reservation]] = room match {
      case Some(r) => reservationService.findAllByRoomId(roomId)
      case _ => Future.successful(List.empty)
    }
    for {
      room <- findById(roomId)
      reservations <- findReservations(room)
      isFree = reservations.forall(_.notIn(period))
    } yield if (room.isDefined) Some(isFree) else None
  }

  def findAllByHotelIds(ids: List[Hotel.id]): Future[List[Room]] =
    for {
      rooms <- roomRepository.findAll()
      inHotels = rooms.filter(ids contains _.hotelId)
    } yield inHotels

  def findAllByHotelId(hotelId: Hotel.id): Future[List[Room]] =
    for {
      rooms <- roomRepository.findAll()
      inHotel = rooms.filter(_.hotelId == hotelId)
    } yield inHotel
}
