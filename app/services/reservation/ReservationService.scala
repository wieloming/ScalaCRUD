package services.reservation

import domain.reservation.Reservation
import domain.reservation.Reservation.Id
import domain.room.Room
import domain.user.User
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import repositories.interfaces.{Errors, ReservationRepo, Validated}

import scala.concurrent.Future

class ReservationService(reservationRepository: ReservationRepo) {

  def create(reservation: Reservation): Future[Either[Errors, Reservation.Id]] = {
    reservationRepository.create(reservation.validate)
  }

  def findByIds(ids: List[Reservation.Id]): Future[Either[Errors, List[Validated[Reservation]]]] = {
    reservationRepository.findByIds(ids)
  }

  def findAllByUserId(id: User.Id): Future[Either[Errors, List[Validated[Reservation]]]] = {
    reservationRepository.findAllForUser(id)
  }

  def findAllByRoomId(id: Room.Id): Future[List[Reservation]] = {
    for {
      reservations <- reservationRepository.findAll()
      inRoom = reservations.filter(_.roomId == id)
    } yield inRoom
  }
}
