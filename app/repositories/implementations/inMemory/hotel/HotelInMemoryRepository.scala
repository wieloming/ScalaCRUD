package repositories.implementations.inMemory.hotel

import java.util.concurrent.atomic.AtomicLong

import domain.DBid
import domain.hotel.Hotel
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.HotelRepo

import scala.concurrent.Future

class HotelInMemoryRepository extends HotelRepo with BaseInMemoryRepository[Hotel, Hotel.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Hotel.Id, Hotel]()

  def create(obj: Hotel): Future[Hotel.Id] = {
    val newId = Hotel.Id(idSequence.incrementAndGet())
    db(newId) = obj
    Future.successful(newId)
  }

  def update(id: Hotel.Id, obj: Hotel): Future[Hotel with DBid[Hotel.Id]] = {
    db(id) = obj
    Future.successful(obj.withId(id))
  }

  def findAllByCity(s: Hotel.City): Future[List[Hotel with DBid[Hotel.Id]]] = {
    val res = db.map{ case (k, v) => v.withId(k)}.filter(_.city == s).toList
    Future.successful(res)
  }
}
