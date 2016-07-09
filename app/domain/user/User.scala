package domain.user

case class User(id: Option[User.Id], email: User.Email)
object User {
  case class Id(value: Long) extends domain.Id
  case class Email(value: String)
}

