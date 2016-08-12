package repositories.interfaces

import domain.reservation.Reservation
import domain.user.User
import utils.ValidDataListOrErrors

trait ReservationRepo extends BaseRepo[Reservation, Reservation.Id] {
   def findAllForUser(id: User.Id): ValidDataListOrErrors[Reservation]
}
