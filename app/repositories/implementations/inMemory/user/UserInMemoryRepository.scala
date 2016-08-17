package repositories.implementations.inMemory.user

import java.util.concurrent.atomic.AtomicLong

import domain.user.User
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces._
import utils.ValueOrErrors


class UserInMemoryRepository extends UserRepo with BaseInMemoryRepository[User, User.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[User.Id, User]()

  def create(valid: Validated[User]): ValueOrErrors[FromDB[User]] = {
    val obj = valid.value
    val newId = User.Id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    ValueOrErrors.data(FromDB(newObj))
  }

  def update(id: User.Id, valid: Validated[User]): ValueOrErrors[FromDB[User]] = {
    val obj = valid.value
    db.get(id) match {
      case Some(user) =>
        val newObj = obj.copy(id = Some(id))
        db(id) = newObj
        ValueOrErrors.data(FromDB(newObj))
      case None => ValueOrErrors.errors(Errors.single("No element with id: " + id + " in db"))
    }
  }
}
