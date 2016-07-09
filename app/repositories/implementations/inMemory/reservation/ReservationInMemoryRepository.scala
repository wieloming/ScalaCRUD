package repositories.implementations.inMemory.reservation

import java.util.concurrent.atomic.AtomicLong

import domain.reservation.Reservation
import domain.user.User
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.ReservationRepo

import scala.concurrent.Future

class ReservationInMemoryRepository extends ReservationRepo with BaseInMemoryRepository[Reservation, Reservation.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Reservation.Id, Reservation]()

  def create(obj: Reservation): Future[Reservation.Id] = {
    val newId = Reservation.Id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    Future.successful(newId)
  }

  def update(id: Reservation.Id, obj: Reservation): Future[Reservation] = {
    val newObj = obj.copy(id = Some(id))
    db(id) = newObj
    Future.successful(newObj)
  }

  def findAllForUser(id: User.Id): Future[List[Reservation]] = {
    Future.successful(db.filter { case (i, r) => r.userId == id }.values.toList)
  }
}
