package repositories.interfaces

import scala.concurrent.Future

trait BaseRepo[T, Id] {

  def create(obj: Validated[T]): Future[Either[Errors,Id]]

  def update(id: Id, el: Validated[T]): Future[Either[Errors,Validated[T]]]

  def findById(id: Id): Future[Either[Errors,Option[Validated[T]]]]

  def findAll(): Future[Either[Errors,List[Validated[T]]]]

  def findByIds(ids: List[Id]): Future[Either[Errors,List[Validated[T]]]]

  def remove(id: Id): Future[Either[Errors,Validated[T]]]
}
