package domain

//TODO: do something with me
trait Id {
  val value: Long
}

//TODO: rename me
trait WithId[T <: Id] {
  val id: Some[T]
}