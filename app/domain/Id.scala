package domain

trait Id {
  val value: Long
}
trait DBid[T] {
  val id: T
}
