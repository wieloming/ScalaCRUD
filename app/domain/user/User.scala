package domain.user

import domain.{ModelForCreate, WithId, Model}
import repositories.interfaces.Validated


case class User(id: User.ModelId, email: User.Email) extends WithId[User]

object User extends Model[User] {
  case class ForCreate(email: Email) extends ModelForCreate[User, User.ModelId] {
    //TODO: validate
    def validate: Validated[User.ForCreate] = Validated(this)
    override def toModel(id: User.ModelId): User = User(id,email)
  }
  case class ModelId(value: Long) extends domain.ModelId[User]
  case class Email(value: String)

}

