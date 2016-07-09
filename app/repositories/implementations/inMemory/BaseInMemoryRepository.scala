package repositories.implementations.inMemory

import java.util.concurrent.atomic.AtomicLong

import repositories.interfaces.BaseRepo

import scala.collection.mutable
import scala.concurrent.Future

trait BaseInMemoryRepository[T, Id] extends BaseRepo[T, Id]{
  protected val db: mutable.Map[Id, T]
  protected val idSequence: AtomicLong

  def create(obj: T): Future[Id]
  def update(id: Id, el: T): Future[T]

  def findById(id: Id): Future[Option[T]] = {
    Future.successful(db.get(id))
  }

  def findAll(): Future[List[T]] = {
    Future.successful(db.values.toList)
  }

  def findByIds(ids: List[Id]): Future[List[T]] = {
    Future.successful(db.filter(r => ids contains r._1).values.toList)
  }

  def remove(id: Id): Future[Boolean] = {
    Future.successful(db.remove(id).isDefined)
  }
}
