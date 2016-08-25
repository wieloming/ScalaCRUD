package repositories.interfaces

import domain.reservation.Reservation
import domain.user.User
import utils.ValueOrErrors

trait ReservationRepo extends BaseRepo[Reservation, Reservation.ForCreate, Reservation.ModelId] {
   def findAllForUser(id: User.ModelId): ValueOrErrors[List[Reservation]]
}
