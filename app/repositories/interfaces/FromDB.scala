package repositories.interfaces

import scala.language.implicitConversions

//TODO: id as field
case class FromDB[T](value: T, id: T#Id)

object FromDB {
  implicit def unwrap[T, G](v: FromDB[T]): T = v.value
}

case class Validated[T](value: T)
