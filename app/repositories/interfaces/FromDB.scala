package repositories.interfaces
import domain.{WithId, Id}

import scala.language.implicitConversions

//TODO: id as field
case class FromDB[T <: WithId[T]](value: T) {
  def id = value.id.get
}

object FromDB {
  implicit def unwrap[T](v: FromDB[T]): T = v.value
}

case class Validated[T](value: T)
