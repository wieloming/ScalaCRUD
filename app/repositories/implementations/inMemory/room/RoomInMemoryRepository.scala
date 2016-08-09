package repositories.implementations.inMemory.room

import java.util.concurrent.atomic.AtomicLong

import domain.room.Room
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.{Errors, RoomRepo, Validated}

import scala.concurrent.Future

class RoomInMemoryRepository extends RoomRepo with BaseInMemoryRepository[Room, Room.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Room.Id, Room]()

  def create(valid: Validated[Room]): Future[Either[Errors, Room.Id]] = {
    val obj = valid.valid
    val newId = Room.Id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    Future.successful(Right(newId))
  }

  def update(id: Room.Id, valid: Validated[Room]): Future[Either[Errors, Validated[Room]]] = {
    val obj = valid.valid
    db.get(id) match {
      case Some(room) =>
        val newObj = obj.copy(id = Some(id))
        db(id) = newObj
        Future.successful(Right(Validated(newObj)))
      case None => Future.successful(Left(Errors.single("No element with id: " + id + " in db")))
    }
  }

}
