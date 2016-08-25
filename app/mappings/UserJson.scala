package mappings

import domain.user.User
import play.api.libs.json._

trait UserJson extends BaseJson {

  implicit val userEmailFormat =
    oneField("email", (s: String) => User.Email(s), (u: User.Email) => u.value)
  implicit val userIdFormat =
    oneField("value", (s: Long) => User.ModelId(s), (u: User.ModelId) => u.value)
  implicit val userForCreateReads =
    oneField("email", (s: String) => User.ForCreate(User.Email(s)), (u: User.ForCreate) => u.email.value)

  implicit val userWritesFormat: Format[User] = Json.format[User]
}
