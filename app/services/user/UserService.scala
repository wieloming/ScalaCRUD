package services.user

import domain.reservation.Reservation
import domain.user.{User, UserForCreateDto}
import repositories.interfaces.{UserRepo, FromDB}
import services.reservation.ReservationService
import utils.ValueOrErrors

class UserService(reservationService: ReservationService, userRepository: UserRepo) {

  def createUser(user: UserForCreateDto): ValueOrErrors[User.ModelId] = {
    userRepository.create(User(None, user.email).validate)
  }

  def findById(id: User.ModelId): ValueOrErrors[FromDB[User, User.ModelId]] = {
    userRepository.findById(id)
  }

  def findReservations(id: User.ModelId): ValueOrErrors[List[FromDB[Reservation, Reservation.ModelId]]] = {
    reservationService.findAllByUserId(id)
  }
}
