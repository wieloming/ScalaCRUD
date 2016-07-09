package services.reservation

import domain.reservation.Reservation
import domain.room.Room
import domain.user.User
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import repositories.interfaces.ReservationRepo

import scala.concurrent.Future

class ReservationService(reservationRepository: ReservationRepo) {

  def create(reservation: Reservation): Future[Reservation.Id] = {
    reservationRepository.create(reservation)
  }

  def findByIds(ids: List[Reservation.Id]): Future[List[Reservation]] = {
    reservationRepository.findByIds(ids)
  }

  def findAllByUserId(id: User.Id): Future[List[Reservation]] = {
    reservationRepository.findAllForUser(id)
  }

  def findAllByRoomId(id: Room.Id): Future[List[Reservation]] = {
    for {
      reservations <- reservationRepository.findAll()
      inRoom = reservations.filter(_.roomId == id)
    } yield inRoom
  }
}
