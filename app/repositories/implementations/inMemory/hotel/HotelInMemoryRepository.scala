package repositories.implementations.inMemory.hotel

import java.util.concurrent.atomic.AtomicLong

import domain.hotel.Hotel
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.{Errors, HotelRepo, Validated}
import utils.ValueOrErrors

class HotelInMemoryRepository extends HotelRepo with BaseInMemoryRepository[Hotel, Hotel.ForCreate, Hotel.ModelId] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Hotel.ModelId, Hotel]()

  def create(valid: Validated[Hotel.ForCreate]): ValueOrErrors[Hotel.ModelId] = {
    val obj = valid.value
    val newId = Hotel.ModelId(idSequence.incrementAndGet())
    val newObj = obj.toModel(id = newId)
    db(newId) = newObj
    ValueOrErrors.data(newId)
  }

  def update(id: Hotel.ModelId, valid: Validated[Hotel.ForCreate]): ValueOrErrors[Hotel] = {
    val obj = valid.value
    db.get(id) match {
      case Some(hotel) =>
        val newObj = obj.toModel(id = id)
        db(id) = newObj
        ValueOrErrors.data(newObj)
      case None => ValueOrErrors.errors(Errors.single("No element with id: " + id + " in db"))
    }
  }

  def findAllByCity(s: Hotel.City): ValueOrErrors[List[Hotel]] = {
    ValueOrErrors.data(db.values.filter(_.city == s).toList)
  }
}
