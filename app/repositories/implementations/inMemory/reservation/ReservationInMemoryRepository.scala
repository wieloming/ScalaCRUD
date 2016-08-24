package repositories.implementations.inMemory.reservation

import java.util.concurrent.atomic.AtomicLong

import domain.reservation.Reservation
import domain.user.User
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces._
import utils.ValueOrErrors

class ReservationInMemoryRepository extends ReservationRepo with BaseInMemoryRepository[Reservation, Reservation.ModelId] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Reservation.ModelId, Reservation]()

  def create(valid: Validated[Reservation]): ValueOrErrors[Reservation.ModelId] = {
    val obj = valid.value
    val newId = Reservation.ModelId(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    ValueOrErrors.data(newId)
  }

  def update(id: Reservation.ModelId, valid: Validated[Reservation]): ValueOrErrors[FromDB[Reservation, Reservation.ModelId]] = {
    val obj = valid.value
    db.get(id) match {
      case Some(reservation) =>
        val newObj = obj.copy(id = Some(id))
        db(id) = newObj
        ValueOrErrors.data(FromDB(newObj, id))
      case None => ValueOrErrors.errors(Errors.single("No element with id: " + id + " in db"))
    }
  }

  def findAllForUser(id: User.ModelId): ValueOrErrors[List[FromDB[Reservation, Reservation.ModelId]]] = {
    ValueOrErrors.data(db.withFilter { case (i, t) => t.userId == id }.map { case (i, t) => FromDB(t, i) }.toList)
  }
}
