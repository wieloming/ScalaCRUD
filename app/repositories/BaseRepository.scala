package repositories

import java.util.concurrent.atomic.AtomicLong

import scala.collection.mutable
import scala.concurrent.Future


trait BaseRepository[T, Tid] {
  protected val db: mutable.Map[Tid, T]
  protected val idSequence: AtomicLong

  def create(obj: T): Future[Tid]
  def update(id: Tid, el: T): Future[T]

  def findById(id: Tid): Future[Option[T]] = {
    Future.successful(db.get(id))
  }

  def findAll(): Future[List[T]] = {
    Future.successful(db.values.toList)
  }

  def findByIds(ids: List[Tid]): Future[List[T]] = {
    Future.successful(db.filter(r => ids contains r._1).values.toList)
  }

  def remove(id: Tid): Future[Boolean] = {
    Future.successful(db.remove(id).isDefined)
  }

}
