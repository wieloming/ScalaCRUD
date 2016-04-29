package services.room

import domain.hotel.Hotel
import domain.reservation.Reservation
import domain.room.Room
import org.joda.time.LocalDate
import repositories.room.RoomRepository
import services.reservation.ReservationService
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

class RoomService(reservationService: ReservationService, roomRepository: RoomRepository) {

  def remove(id: Room.id) = roomRepository.remove(id)

  def create(room: Room) = roomRepository.create(room: Room)

  def isFreeBetween(roomId: Room.id, from: LocalDate, to: LocalDate): Future[Option[Boolean]] = {
    def findReservations(room: Option[Room]): Future[List[Reservation]] = room match {
      case Some(r) => reservationService.findAllByRoomId(roomId)
      case _ => Future.successful(List.empty)
    }
    for {
      room <- findById(roomId)
      reservations <- findReservations(room)
    } yield if (room.isDefined) Some(reservations.forall(_.notIn(from, to))) else None
  }

  def findByIds(ids: List[Room.id]): Future[List[Room]] = {
    roomRepository.findByIds(ids)
  }

  def findById(id: Room.id): Future[Option[Room]] = {
    roomRepository.findById(id)
  }

  def findByHotelIds(ids: List[Hotel.id]): Future[List[Room]] = {
    for {
      rooms <- roomRepository.findAll()
      inHotels = rooms.filter(ids contains _.hotelId)
    } yield inHotels
  }

  def findByHotelId(hotelId: Hotel.id): Future[List[Room]] = {
    for {
      rooms <- roomRepository.findAll()
      inHotel = rooms.filter(_.hotelId == hotelId)
    } yield inHotel
  }
}
