package services.user

import domain.reservation.Reservation
import domain.user.{User, UserForCreateDto}
import repositories.interfaces.UserRepo
import services.reservation.ReservationService
import utils.{ValidDataListOrErrors, ValidDataOrErrors}

class UserService(reservationService: ReservationService, userRepository: UserRepo) {

  def createUser(user: UserForCreateDto): ValidDataOrErrors[User] = {
    userRepository.create(User(None, user.email).validate)
  }

  def findById(id: User.Id): ValidDataOrErrors[User] = {
    userRepository.findById(id)
  }

  def findReservations(id: User.Id): ValidDataListOrErrors[Reservation] = {
    reservationService.findAllByUserId(id)
  }
}
