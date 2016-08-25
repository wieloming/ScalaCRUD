package repositories.implementations.inMemory.room

import java.util.concurrent.atomic.AtomicLong

import domain.room.Room
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces._
import utils.ValueOrErrors

class RoomInMemoryRepository extends RoomRepo with BaseInMemoryRepository[Room,Room.ForCreate, Room.ModelId] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Room.ModelId, Room]()


  def create(valid: Validated[Room.ForCreate]): ValueOrErrors[Room.ModelId] = {
    val obj = valid.value
    val newId = Room.ModelId(idSequence.incrementAndGet())
    val newObj = obj.toModel(id = newId)
    db(newId) = newObj
    ValueOrErrors.data(newId)
  }

  def update(id: Room.ModelId, valid: Validated[Room.ForCreate]): ValueOrErrors[Room] = {
    val obj = valid.value
    db.get(id) match {
      case Some(room) =>
        val newObj = obj.toModel(id = id)
        db(id) = newObj
        ValueOrErrors.data(newObj)
      case None => ValueOrErrors.errors(Errors.single("No element with id: " + id + " in db"))
    }
  }
}
