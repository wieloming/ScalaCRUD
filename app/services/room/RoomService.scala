package services.room

import domain.hotel.Hotel
import domain.reservation.Reservation
import domain.room.Room
import domain.room.Room.Id
import services.reservation.ReservationService
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import repositories.interfaces.{Errors, RoomRepo, Validated}
import utils.{ValidDataListOrErrors, ValidDataOrErrors}

import scala.concurrent.Future

class RoomService(reservationService: ReservationService, roomRepository: RoomRepo) {

  def remove(id: Room.Id): ValidDataOrErrors[Room] = roomRepository.remove(id)

  def create(room: Room): ValidDataOrErrors[Room] = roomRepository.create(room.validate)

  def findByIds(ids: List[Room.Id]): ValidDataListOrErrors[Room] =
    roomRepository.findByIds(ids)

  def findById(id: Room.Id): ValidDataOrErrors[Room] =
    roomRepository.findById(id)

  def isFreeBetween(roomId: Room.Id, period: Reservation.Period): Future[Option[Boolean]] = {
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

  def findAllByHotelIds(ids: Hotel.Id *): ValidDataListOrErrors[Room] =
    for {
      room <- roomRepository.findAll() if ids contains room.hotelId
    } yield room

}
