package repositories.implementations.file.reservation

import java.util.concurrent.atomic.AtomicLong

import domain.reservation.Reservation
import domain.user.User
import repositories.implementations.file.BaseFileRepository
import repositories.interfaces.ReservationRepo

import scala.collection.mutable
import scala.concurrent.Future

class ReservationFileRepository extends ReservationRepo with BaseFileRepository[Reservation, Reservation.id] {
  override val idSequence = new AtomicLong(0)
  override val db = mutable.Map[Reservation.id, Reservation]()

  def create(obj: Reservation): Future[Reservation.id] = {
    val newId = Reservation.id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    Future.successful(newId)
  }

  def update(id: Reservation.id, obj: Reservation): Future[Reservation] = {
    val newObj = obj.copy(id = Some(id))
    db(id) = newObj
    Future.successful(newObj)
  }

  def findAllForUser(id: User.id): Future[List[Reservation]] = {
    Future.successful(db.filter { case (i, r) => r.userId == id }.values.toList)
  }
}
