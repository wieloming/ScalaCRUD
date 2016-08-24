package domain.user

import domain.Model
import repositories.interfaces.Validated


case class User(id: Option[User.ModelId], email: User.Email) {
  //TODO: validate
  def validate: Validated[User] = Validated(this)
}

object User extends Model[User] {
  case class ModelId(value: Long) extends domain.ModelId
  case class Email(value: String)

}

