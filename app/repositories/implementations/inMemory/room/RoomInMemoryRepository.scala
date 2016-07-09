package repositories.implementations.inMemory.room

import java.util.concurrent.atomic.AtomicLong

import domain.room.Room
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.RoomRepo

import scala.concurrent.Future

class RoomInMemoryRepository extends RoomRepo with BaseInMemoryRepository[Room, Room.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Room.Id, Room]()

  def create(obj: Room): Future[Room.Id] = {
    val newId = Room.Id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    Future.successful(newId)
  }

  def update(id: Room.Id, obj: Room): Future[Room] = {
    val newObj = obj.copy(id = Some(id))
    db(id) = newObj
    Future.successful(newObj)
  }
}
