package services.reservation

import domain.reservation.Reservation
import domain.room.Room
import domain.user.User
import repositories.interfaces.{ReservationRepo, FromDB}
import utils.ValueOrErrors

class ReservationService(reservationRepository: ReservationRepo) {

  def create(reservation: Reservation): ValueOrErrors[Reservation.ModelId] = {
    reservationRepository.create(reservation.validate)
  }

  def findByIds(ids: List[Reservation.ModelId]): ValueOrErrors[List[FromDB[Reservation, Reservation.ModelId]]] = {
    reservationRepository.findByIds(ids)
  }

  def findAllByUserId(id: User.ModelId): ValueOrErrors[List[FromDB[Reservation, Reservation.ModelId]]] = {
    reservationRepository.findAllForUser(id)
  }

  def findAllByRoomId(id: Room.ModelId): ValueOrErrors[List[FromDB[Reservation, Reservation.ModelId]]] = {
    reservationRepository.findAll().map(_.filter(_.roomId == id))
  }
}
