package domain

//TODO: do something with me
trait ModelId {
  val value: Long
}

trait Model[T] {
  type Id <: ModelId
}
