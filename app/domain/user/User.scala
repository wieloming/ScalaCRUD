package domain.user

import domain.Id


case class User(id: Option[User.id], email: String)
object User {
  case class id(value: Long) extends Id
}

