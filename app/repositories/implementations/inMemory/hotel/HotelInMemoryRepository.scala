package repositories.implementations.inMemory.hotel

import java.util.concurrent.atomic.AtomicLong

import domain.hotel.Hotel
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.{Errors, HotelRepo, Validated}
import utils.{ValidDataListOrErrors, ValidDataOrErrors}

class HotelInMemoryRepository extends HotelRepo with BaseInMemoryRepository[Hotel, Hotel.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[Hotel.Id, Hotel]()

  def create(valid: Validated[Hotel]): ValidDataOrErrors[Hotel] = {
    val obj = valid.valid
    val newId = Hotel.Id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    ValidDataOrErrors.data(Validated(newObj))
  }

  def update(id: Hotel.Id, valid: Validated[Hotel]): ValidDataOrErrors[Hotel] = {
    val obj = valid.valid
    db.get(id) match {
      case Some(hotel) =>
        val newObj = obj.copy(id = Some(id))
        db(id) = newObj
        ValidDataOrErrors.data(Validated(newObj))
      case None => ValidDataOrErrors.errors(Errors.single("No element with id: " + id + " in db"))
    }
  }

  def findAllByCity(s: Hotel.City): ValidDataListOrErrors[Hotel] = {
    ValidDataListOrErrors.data(db.values.filter(_.city == s).map(Validated(_)).toList)
  }
}
