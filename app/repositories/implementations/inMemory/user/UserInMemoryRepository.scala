package repositories.implementations.inMemory.user

import java.util.concurrent.atomic.AtomicLong

import domain.user.User
import repositories.implementations.inMemory.BaseInMemoryRepository
import repositories.interfaces.UserRepo

import scala.concurrent.Future


class UserInMemoryRepository extends UserRepo with BaseInMemoryRepository[User, User.Id] {
  override val idSequence = new AtomicLong(0)
  override val db = scala.collection.concurrent.TrieMap[User.Id, User]()

  def create(obj: User): Future[User.Id] = {
    val newId = User.Id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    Future.successful(newId)
  }

  def update(id: User.Id, obj: User): Future[User] = {
    val newObj = obj.copy(id = Some(id))
    db(id) = newObj
    Future.successful(newObj)
  }
}
