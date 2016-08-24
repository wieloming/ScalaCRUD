package repositories.interfaces

import domain.Model

import scala.language.implicitConversions

case class FromDB[T, I <: Model[T]](value: T, id: I#Id)

object FromDB {
  implicit def unwrap[T, I <: Model[T]](v: FromDB[T, I]): T = v.value
}

case class Validated[T](value: T)
