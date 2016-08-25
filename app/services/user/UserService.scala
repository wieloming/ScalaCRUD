package services.user

import domain.reservation.Reservation
import domain.user.User
import repositories.interfaces.UserRepo
import services.reservation.ReservationService
import utils.ValueOrErrors

class UserService(reservationService: ReservationService, userRepository: UserRepo) {

  def createUser(user: User.ForCreate): ValueOrErrors[User.ModelId] = {
    userRepository.create(user.validate)
  }

  def findById(id: User.ModelId): ValueOrErrors[User] = {
    userRepository.findById(id)
  }

  def findReservations(id: User.ModelId): ValueOrErrors[List[Reservation]] = {
    reservationService.findAllByUserId(id)
  }
}
