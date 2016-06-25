package repositories.implementations.file.room

import java.util.concurrent.atomic.AtomicLong

import domain.room.Room
import repositories.implementations.file.BaseFileRepository
import repositories.interfaces.RoomRepo

import scala.concurrent.Future

class RoomFileRepository extends RoomRepo with BaseFileRepository[Room, Room.id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Room.id, Room]()

  def create(obj: Room): Future[Room.id] = {
    val newId = Room.id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    Future.successful(newId)
  }

  def update(id: Room.id, obj: Room): Future[Room] = {
    val newObj = obj.copy(id = Some(id))
    db(id) = newObj
    Future.successful(newObj)
  }
}
