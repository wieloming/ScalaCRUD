package repositories.implementations.inMemory.reservation

import java.util.concurrent.atomic.AtomicLong

import domain.reservation.Reservation
import domain.user.User
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.{Errors, ReservationRepo, Validated}
import utils.{ValidDataListOrErrors, ValidDataOrErrors}

class ReservationInMemoryRepository extends ReservationRepo with BaseInMemoryRepository[Reservation, Reservation.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Reservation.Id, Reservation]()

  def create(valid: Validated[Reservation]): ValidDataOrErrors[Reservation] = {
    val obj = valid.valid
    val newId = Reservation.Id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    ValidDataOrErrors.data(Validated(newObj))
  }

  def update(id: Reservation.Id, valid: Validated[Reservation]): ValidDataOrErrors[Reservation] = {
    val obj = valid.valid
    db.get(id) match {
      case Some(reservation) =>
        val newObj = obj.copy(id = Some(id))
        db(id) = newObj
        ValidDataOrErrors.data(Validated(newObj))
      case None => ValidDataOrErrors.errors(Errors.single("No element with id: " + id + " in db"))
    }
  }

  def findAllForUser(id: User.Id): ValidDataListOrErrors[Reservation] = {
    ValidDataListOrErrors.data(db.values.filter(_.userId == id).map(Validated(_)).toList)
  }
}
