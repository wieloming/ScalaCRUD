package repositories.interfaces

import domain.reservation.Reservation
import domain.user.User

import scala.concurrent.Future

trait ReservationRepo extends BaseRepo[Reservation, Reservation.id] {
   def findAllForUser(id: User.id): Future[List[Reservation]]
}
