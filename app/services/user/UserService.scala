package services.user

import domain.reservation.Reservation
import domain.user.{User, UserForCreateDto}
import repositories.interfaces.{Errors, UserRepo, Validated}
import services.reservation.ReservationService

import scala.concurrent.Future

class UserService(reservationService: ReservationService, userRepository: UserRepo) {

  def createUser(user: UserForCreateDto): Future[Either[Errors, User.Id]] = {
    userRepository.create(User(None, user.email).validate)
  }

  def findById(id: User.Id): Future[Either[Errors, Option[Validated[User]]]] = {
    userRepository.findById(id)
  }

  def findReservations(id: User.Id): Future[Either[Errors, List[Validated[Reservation]]]] = {
    reservationService.findAllByUserId(id)
  }
}
