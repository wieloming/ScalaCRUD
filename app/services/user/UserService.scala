package services.user

import domain.reservation.Reservation
import domain.user.{User, UserForCreateDto}
import services.reservation.ReservationService
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import repositories.interfaces.UserRepo

import scala.concurrent.Future

class UserService(reservationService: ReservationService, userRepository: UserRepo) {

  def createUser(user: UserForCreateDto): Future[User.id] = {
    userRepository.create(User(None, user.email))
  }

  def findById(id: User.id): Future[Option[User]] = {
    userRepository.findAll().foreach(o => println(">>"+o))
    userRepository.findById(id)
  }

  def findReservations(id: User.id): Future[List[Reservation]] = {
    for {
      userId <- userRepository.findById(id).map(_.flatMap(_.id))
      reservations <- Future.traverse(userId.toList)(reservationService.findAllForUser)
    } yield reservations.flatten
  }
}
