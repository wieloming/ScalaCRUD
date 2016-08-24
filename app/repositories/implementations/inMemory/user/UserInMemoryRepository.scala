package repositories.implementations.inMemory.user

import java.util.concurrent.atomic.AtomicLong

import domain.user.User
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces._
import utils.ValueOrErrors


class UserInMemoryRepository extends UserRepo with BaseInMemoryRepository[User, User.ModelId] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[User.ModelId, User]()

  def create(valid: Validated[User]): ValueOrErrors[User.ModelId] = {
    val obj = valid.value
    val newId = User.ModelId(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    ValueOrErrors.data(newId)
  }

  def update(id: User.ModelId, valid: Validated[User]): ValueOrErrors[FromDB[User, User.ModelId]] = {
    val obj = valid.value
    db.get(id) match {
      case Some(user) =>
        val newObj = obj.copy(id = Some(id))
        db(id) = newObj
        ValueOrErrors.data(FromDB(newObj, id))
      case None => ValueOrErrors.errors(Errors.single("No element with id: " + id + " in db"))
    }
  }
}
