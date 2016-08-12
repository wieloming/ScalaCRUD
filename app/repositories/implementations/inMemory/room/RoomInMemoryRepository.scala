package repositories.implementations.inMemory.room

import java.util.concurrent.atomic.AtomicLong

import domain.room.Room
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.{Errors, RoomRepo, Validated}
import utils.ValidDataOrErrors

class RoomInMemoryRepository extends RoomRepo with BaseInMemoryRepository[Room, Room.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Room.Id, Room]()


  def create(valid: Validated[Room]): ValidDataOrErrors[Room] = {
    val obj = valid.valid
    val newId = Room.Id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    ValidDataOrErrors.data(Validated(newObj))
  }

  def update(id: Room.Id, valid: Validated[Room]): ValidDataOrErrors[Room] = {
    val obj = valid.valid
    db.get(id) match {
      case Some(room) =>
        val newObj = obj.copy(id = Some(id))
        db(id) = newObj
        ValidDataOrErrors.data(Validated(newObj))
      case None => ValidDataOrErrors.errors(Errors.single("No element with id: " + id + " in db"))
    }
  }
}
