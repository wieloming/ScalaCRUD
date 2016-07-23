package repositories.interfaces

import scala.concurrent.Future

trait BaseRepo[T, Id] {

  def create(obj: T): Future[Id]

  def update(id: Id, el: T): Future[T with Id]

  def findById(id: Id): Future[Option[T with Id]]

  def findAll(): Future[List[T with Id]]

  def findByIds(ids: List[Id]): Future[List[T with Id]]

  def remove(id: Id): Future[Boolean]
}
