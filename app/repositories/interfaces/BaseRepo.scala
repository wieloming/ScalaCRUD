package repositories.interfaces

import scala.concurrent.Future

trait BaseRepo[T, Id] {

  def create(obj: T): Future[Id]

  def update(id: Id, el: T): Future[T]

  def findById(id: Id): Future[Option[T]]

  def findAll(): Future[List[T]]

  def findByIds(ids: List[Id]): Future[List[T]]

  def remove(id: Id): Future[Boolean]
}
