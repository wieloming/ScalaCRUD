package repositories.implementations.file.user

import domain.user.User

import collection.mutable
import java.util.concurrent.atomic.AtomicLong

import repositories.implementations.file.BaseFileRepository
import repositories.interfaces.UserRepo

import scala.concurrent.Future


class UserFileRepository extends UserRepo with BaseFileRepository[User, User.id] {
  override val idSequence = new AtomicLong(0)
  override val db = mutable.Map[User.id, User]()

  def create(obj: User): Future[User.id] = {
    val newId = User.id(idSequence.incrementAndGet())
    val newObj = obj.copy(id = Some(newId))
    db(newId) = newObj
    Future.successful(newId)
  }

  def update(id: User.id, obj: User): Future[User] = {
    val newObj = obj.copy(id = Some(id))
    db(id) = newObj
    Future.successful(newObj)
  }
}
