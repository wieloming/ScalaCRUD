package repositories.interfaces

import domain.Model
import utils.ValueOrErrors

trait BaseRepo[T, I <: Model[T]] {

  def create(obj: Validated[T]): ValueOrErrors[I#Id]

  def update(id: I#Id, el: Validated[T]): ValueOrErrors[FromDB[T, I#Id]]

  def findById(id: I#Id): ValueOrErrors[FromDB[T, I#Id]]

  def findAll(): ValueOrErrors[List[FromDB[T, I#Id]]]

  def findByIds(ids: List[I#Id]): ValueOrErrors[List[FromDB[T, I#Id]]]

  def remove(id: I#Id): ValueOrErrors[FromDB[T, I#Id]]
}
