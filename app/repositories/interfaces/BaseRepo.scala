package repositories.interfaces

import utils.{ValidDataListOrErrors, ValidDataOrErrors}

trait BaseRepo[T, Id] {

  def create(obj: Validated[T]): ValidDataOrErrors[T]

  def update(id: Id, el: Validated[T]): ValidDataOrErrors[T]

  def findById(id: Id): ValidDataOrErrors[T]

  def findAll(): ValidDataListOrErrors[T]

  def findByIds(ids: List[Id]): ValidDataListOrErrors[T]

  def remove(id: Id): ValidDataOrErrors[T]
}
