package repositories.implementations.inMemory.hotel

import java.util.concurrent.atomic.AtomicLong

import domain.hotel.Hotel
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.{Errors, FromDB, HotelRepo, Validated}
import utils.ValueOrErrors

class HotelInMemoryRepository extends HotelRepo with BaseInMemoryRepository[Hotel, Hotel.ModelId] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Hotel.ModelId, Hotel]()

  def create(valid: Validated[Hotel]): ValueOrErrors[Hotel.ModelId] = {
    val obj = valid.value
    val newId = Hotel.ModelId(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    ValueOrErrors.data(newId)
  }

  def update(id: Hotel.ModelId, valid: Validated[Hotel]) = {
    val obj = valid.value
    db.get(id) match {
      case Some(hotel) =>
        val newObj = obj.copy(id = Some(id))
        db(id) = newObj
        ValueOrErrors.data(FromDB[Hotel, Hotel.type](newObj, id))
      case None => ValueOrErrors.errors(Errors.single("No element with id: " + id + " in db"))
    }
  }

  def findAllByCity(s: Hotel.City): ValueOrErrors[List[FromDB[Hotel, Hotel.ModelId]]] = {
    ValueOrErrors.data(db.withFilter { case (i, h) => h.city == s }.map { case (i, h) => FromDB(h, i) }.toList)
  }
}
