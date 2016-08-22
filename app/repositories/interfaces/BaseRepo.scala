package repositories.interfaces

import domain.WithId
import utils.ValueOrErrors

trait BaseRepo[T <: WithId[T], Id] {

  def create(obj: Validated[T]): ValueOrErrors[FromDB[T]]

  def update(id: Id, el: Validated[T]): ValueOrErrors[FromDB[T]]

  def findById(id: Id): ValueOrErrors[FromDB[T]]

  def findAll(): ValueOrErrors[List[FromDB[T]]]

  def findByIds(ids: List[Id]): ValueOrErrors[List[FromDB[T]]]

  def remove(id: Id): ValueOrErrors[FromDB[T]]
}
