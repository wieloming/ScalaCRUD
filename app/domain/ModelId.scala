package domain

import repositories.interfaces.Validated

//TODO: do something with me
trait ModelId[T] {
  val value: Long
}
trait ModelForCreate[T, Id <: ModelId] {
  def validate: Validated[this.type]
  def toModel(i: Id): T
}

trait Model[T] {
  type Id <: ModelId
  type ForCreate <: ModelForCreate[T, Id]
}

trait WithId[T] {
  val id: ModelId[T]
}