package domain.user


case class User(id: Option[User.id], email: String)
object User {
  case class id(value: Long) extends AnyVal
}

