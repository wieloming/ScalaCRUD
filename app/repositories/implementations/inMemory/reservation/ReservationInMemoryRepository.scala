package repositories.implementations.inMemory.reservation

import java.util.concurrent.atomic.AtomicLong

import domain.reservation.Reservation
import domain.user.User
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces._
import utils.ValueOrErrors

class ReservationInMemoryRepository extends ReservationRepo with BaseInMemoryRepository[Reservation, Reservation.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Reservation.Id, Reservation]()

  def create(valid: Validated[Reservation]): ValueOrErrors[FromDB[Reservation]] = {
    val obj = valid.value
    val newId = Reservation.Id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    ValueOrErrors.data(FromDB(newObj))
  }

  def update(id: Reservation.Id, valid: Validated[Reservation]): ValueOrErrors[FromDB[Reservation]] = {
    val obj = valid.value
    db.get(id) match {
      case Some(reservation) =>
        val newObj = obj.copy(id = Some(id))
        db(id) = newObj
        ValueOrErrors.data(FromDB(newObj))
      case None => ValueOrErrors.errors(Errors.single("No element with id: " + id + " in db"))
    }
  }

  def findAllForUser(id: User.Id): ValueOrErrors[List[FromDB[Reservation]]] = {
    ValueOrErrors.data(db.values.filter(_.userId == id).map(FromDB(_)).toList)
  }
}
