package domain.user

import domain.Id


case class User(id: Option[User.id], email: User.email)
object User {
  case class id(value: Long) extends Id
  case class email(value: String)
}

