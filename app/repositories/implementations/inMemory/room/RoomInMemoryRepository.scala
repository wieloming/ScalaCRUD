package repositories.implementations.inMemory.room

import java.util.concurrent.atomic.AtomicLong

import domain.room.Room
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces._
import utils.ValueOrErrors

class RoomInMemoryRepository extends RoomRepo with BaseInMemoryRepository[Room, Room.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Room.Id, Room]()


  def create(valid: Validated[Room]): ValueOrErrors[FromDB[Room]] = {
    val obj = valid.value
    val newId = Room.Id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    ValueOrErrors.data(FromDB(newObj))
  }

  def update(id: Room.Id, valid: Validated[Room]): ValueOrErrors[FromDB[Room]] = {
    val obj = valid.value
    db.get(id) match {
      case Some(room) =>
        val newObj = obj.copy(id = Some(id))
        db(id) = newObj
        ValueOrErrors.data(FromDB(newObj))
      case None => ValueOrErrors.errors(Errors.single("No element with id: " + id + " in db"))
    }
  }
}
