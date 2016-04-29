package services.reservation

import domain.reservation.Reservation
import domain.room.Room
import domain.user.User
import repositories.reservation.ReservationRepository
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

class ReservationService(reservationRepository: ReservationRepository) {
  def create(reservation: Reservation): Future[Reservation.id] = {
    reservationRepository.create(reservation)
  }
  def findByIds(ids: List[Reservation.id]): Future[List[Reservation]] = {
    reservationRepository.findByIds(ids)
  }
  def findAllForUser(id: User.id): Future[List[Reservation]] = {
    reservationRepository.findAllForUser(id)
  }
  def findAllByRoomId(id: Room.id): Future[List[Reservation]] = {
    for {
      reservations <- reservationRepository.findAll()
      inRoom = reservations.filter(_.roomId == id)
    } yield inRoom
  }
}
