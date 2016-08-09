package repositories.implementations.inMemory.hotel

import java.util.concurrent.atomic.AtomicLong

import domain.hotel.Hotel
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.{Errors, HotelRepo, Validated}

import scala.concurrent.Future

class HotelInMemoryRepository extends HotelRepo with BaseInMemoryRepository[Hotel, Hotel.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Hotel.Id, Hotel]()

  def create(valid: Validated[Hotel]): Future[Either[Errors, Hotel.Id]] = {
    val obj = valid.valid
    val newId = Hotel.Id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    Future.successful(Right(newId))
  }

  def update(id: Hotel.Id, valid: Validated[Hotel]): Future[Either[Errors, Validated[Hotel]]] = {
    val obj = valid.valid
    db.get(id) match {
      case Some(hotel) =>
        val newObj = obj.copy(id = Some(id))
        db(id) = newObj
        Future.successful(Right(Validated(newObj)))
      case None => Future.successful(Left(Errors.single("No element with id: " + id + " in db")))
    }
  }

  def findAllByCity(s: Hotel.City): Future[Either[Errors, List[Hotel]]] = {
    Future.successful(Right(db.values.filter(_.city == s).toList))
  }
}
