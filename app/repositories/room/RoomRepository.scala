package repositories.room

import java.util.concurrent.atomic.AtomicLong

import domain.room.Room
import repositories.BaseRepository

import scala.collection.mutable
import scala.concurrent.Future

class RoomRepository extends BaseRepository[Room, Room.id] {
  override val idSequence = new AtomicLong(0)
  override val db = mutable.Map[Room.id, Room]()

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
