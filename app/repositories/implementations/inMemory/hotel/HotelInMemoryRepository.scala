package repositories.implementations.inMemory.hotel

import java.util.concurrent.atomic.AtomicLong

import domain.hotel.Hotel
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.HotelRepo

import scala.concurrent.Future

class HotelInMemoryRepository extends HotelRepo with BaseInMemoryRepository[Hotel, Hotel.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Hotel.Id, Hotel]()

  def create(obj: Hotel): Future[Hotel.Id] = {
    val newId = Hotel.Id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    Future.successful(newId)
  }

  def update(id: Hotel.Id, obj: Hotel): Future[Hotel] = {
    val newObj = obj.copy(id = Some(id))
    db(id) = newObj
    Future.successful(newObj)
  }

  def findAllByCity(s: Hotel.City): Future[List[Hotel]] = {
    Future.successful(db.values.filter(_.city == s).toList)
  }
}
