package repositories.implementations.inMemory.reservation

import java.util.concurrent.atomic.AtomicLong

import domain.reservation.Reservation
import domain.user.User
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.{Errors, ReservationRepo, Validated}

import scala.concurrent.Future

class ReservationInMemoryRepository extends ReservationRepo with BaseInMemoryRepository[Reservation, Reservation.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Reservation.Id, Reservation]()

  def create(valid: Validated[Reservation]): Future[Either[Errors, Reservation.Id]] = {
    val obj = valid.valid
    val newId = Reservation.Id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    Future.successful(Right(newId))
  }

  def update(id: Reservation.Id, valid: Validated[Reservation]): Future[Either[Errors, Validated[Reservation]]] = {
    val obj = valid.valid
    db.get(id) match {
      case Some(reservation) =>
        val newObj = obj.copy(id = Some(id))
        db(id) = newObj
        Future.successful(Right(Validated(newObj)))
      case None => Future.successful(Left(Errors.single("No element with id: " + id + " in db")))
    }
  }

  def findAllForUser(id: User.Id): Future[Either[Errors,List[Reservation]]] = {
    Future.successful(Right(db.filter { case (i, r) => r.userId == id }.values.toList))
  }
}
