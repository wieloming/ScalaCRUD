package domain.user

import repositories.interfaces.Validated

case class User(id: Option[User.Id], email: User.Email) {
  //TODO: validate
  def validate: Validated[User] = Validated(this)
}
object User {
  case class Id(value: Long) extends domain.Id
  case class Email(value: String)
}

