package repositories.implementations.inMemory.user

import java.util.concurrent.atomic.AtomicLong

import domain.user.User
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.{Errors, UserRepo, Validated}
import utils.ValidDataOrErrors


class UserInMemoryRepository extends UserRepo with BaseInMemoryRepository[User, User.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[User.Id, User]()

  def create(valid: Validated[User]): ValidDataOrErrors[User] = {
    val obj = valid.valid
    val newId = User.Id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    ValidDataOrErrors.data(Validated(newObj))
  }

  def update(id: User.Id, valid: Validated[User]): ValidDataOrErrors[User] = {
    val obj = valid.valid
    db.get(id) match {
      case Some(user) =>
        val newObj = obj.copy(id = Some(id))
        db(id) = newObj
        ValidDataOrErrors.data(Validated(newObj))
      case None => ValidDataOrErrors.errors(Errors.single("No element with id: " + id + " in db"))
    }
  }
}
