package repositories.hotel

import java.util.concurrent.atomic.AtomicLong

import domain.hotel.Hotel
import repositories.BaseRepository

import scala.collection.mutable
import scala.concurrent.Future

class HotelRepository extends BaseRepository[Hotel, Hotel.id] {
  override val idSequence = new AtomicLong(0)
  override val db = mutable.Map[Hotel.id, Hotel]()

  def create(obj: Hotel): Future[Hotel.id] = {
    val newId = Hotel.id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    Future.successful(newId)
  }

  def update(id: Hotel.id, obj: Hotel): Future[Hotel] = {
    val newObj = obj.copy(id = Some(id))
    db(id) = newObj
    Future.successful(newObj)
  }

  def findAllByCity(s: String): Future[List[Hotel]] = {
    Future.successful(db.values.filter(_.city == s).toList)
  }
}
