package repositories.interfaces

import domain.reservation.Reservation
import domain.user.User
import utils.ValueOrErrors

trait ReservationRepo extends BaseRepo[Reservation, Reservation.Id] {
   def findAllForUser(id: User.Id): ValueOrErrors[List[FromDB[Reservation]]]
}
