package repositories.interfaces

import domain.reservation.Reservation
import domain.user.User

import scala.concurrent.Future

trait ReservationRepo extends BaseRepo[Reservation, Reservation.Id] {
   def findAllForUser(id: User.Id): Future[Either[Errors,List[Validated[Reservation]]]]
}
