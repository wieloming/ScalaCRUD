package repositories.implementations.inMemory.hotel

import java.util.concurrent.atomic.AtomicLong

import domain.hotel.Hotel
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.{Errors, FromDB, HotelRepo, Validated}
import utils.ValueOrErrors

class HotelInMemoryRepository extends HotelRepo with BaseInMemoryRepository[Hotel, Hotel.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Hotel.Id, Hotel]()

  def create(valid: Validated[Hotel]): ValueOrErrors[FromDB[Hotel]] = {
    val obj = valid.value
    val newId = Hotel.Id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    ValueOrErrors.data(FromDB(newObj))
  }

  def update(id: Hotel.Id, valid: Validated[Hotel]): ValueOrErrors[FromDB[Hotel]] = {
    val obj = valid.value
    db.get(id) match {
      case Some(hotel) =>
        val newObj = obj.copy(id = Some(id))
        db(id) = newObj
        ValueOrErrors.data(FromDB(newObj))
      case None => ValueOrErrors.errors(Errors.single("No element with id: " + id + " in db"))
    }
  }

  def findAllByCity(s: Hotel.City): ValueOrErrors[List[FromDB[Hotel]]] = {
    ValueOrErrors.data(db.values.filter(_.city == s).map(FromDB(_)).toList)
  }
}
