package repositories.interfaces

import domain.{ModelId, ModelForCreate}
import utils.ValueOrErrors

trait BaseRepo[T, G <: ModelForCreate[T, Id], Id <: ModelId] {

  def create(obj: Validated[G]): ValueOrErrors[Id]

  def update(id: Id, el: Validated[G]): ValueOrErrors[T]

  def findById(id: Id): ValueOrErrors[T]

  def findAll(): ValueOrErrors[List[T]]

  def findByIds(ids: List[Id]): ValueOrErrors[List[T]]

  def remove(id: Id): ValueOrErrors[T]
}
