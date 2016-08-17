package services.reservation

import domain.reservation.Reservation
import domain.room.Room
import domain.user.User
import repositories.interfaces.{ReservationRepo, FromDB}
import utils.ValueOrErrors

class ReservationService(reservationRepository: ReservationRepo) {

  def create(reservation: Reservation): ValueOrErrors[FromDB[Reservation]] = {
    reservationRepository.create(reservation.validate)
  }

  def findByIds(ids: List[Reservation.Id]): ValueOrErrors[List[FromDB[Reservation]]] = {
    reservationRepository.findByIds(ids)
  }

  def findAllByUserId(id: User.Id): ValueOrErrors[List[FromDB[Reservation]]] = {
    reservationRepository.findAllForUser(id)
  }

  def findAllByRoomId(id: Room.Id): ValueOrErrors[List[FromDB[Reservation]]] = {
    reservationRepository.findAll().map(_.filter(_.roomId == id))
  }
}
