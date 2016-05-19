package repositories.implementations.file.hotel

import java.util.concurrent.atomic.AtomicLong

import domain.hotel.Hotel
import repositories.implementations.file.BaseFileRepository
import repositories.interfaces.HotelRepo

import scala.collection.mutable
import scala.concurrent.Future

class HotelFileRepository extends HotelRepo with BaseFileRepository[Hotel, Hotel.id] {
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

  def findAllByCity(s: Hotel.city): Future[List[Hotel]] = {
    Future.successful(db.values.filter(_.city == s).toList)
  }
}
