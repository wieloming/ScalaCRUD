package services.reservation

import domain.reservation.Reservation
import domain.room.Room
import domain.user.User
import repositories.interfaces.ReservationRepo
import utils.{ValidDataListOrErrors, ValidDataOrErrors}

class ReservationService(reservationRepository: ReservationRepo) {

  def create(reservation: Reservation): ValidDataOrErrors[Reservation] = {
    reservationRepository.create(reservation.validate)
  }

  def findByIds(ids: List[Reservation.Id]): ValidDataListOrErrors[Reservation] = {
    reservationRepository.findByIds(ids)
  }

  def findAllByUserId(id: User.Id): ValidDataListOrErrors[Reservation] = {
    reservationRepository.findAllForUser(id)
  }

  def findAllByRoomId(id: Room.Id): ValidDataListOrErrors[Reservation] = {
    for {
      reservation <- reservationRepository.findAll() if reservation.roomId == id
    } yield reservation
  }
}
