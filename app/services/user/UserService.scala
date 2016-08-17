package services.user

import domain.reservation.Reservation
import domain.user.{User, UserForCreateDto}
import repositories.interfaces.{UserRepo, FromDB}
import services.reservation.ReservationService
import utils.ValueOrErrors

class UserService(reservationService: ReservationService, userRepository: UserRepo) {

  def createUser(user: UserForCreateDto): ValueOrErrors[FromDB[User]] = {
    userRepository.create(User(None, user.email).validate)
  }

  def findById(id: User.Id): ValueOrErrors[FromDB[User]] = {
    userRepository.findById(id)
  }

  def findReservations(id: User.Id): ValueOrErrors[List[FromDB[Reservation]]] = {
    reservationService.findAllByUserId(id)
  }
}
