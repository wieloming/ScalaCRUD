package mappings

import domain.user.{User, UserForCreateDto}
import play.api.libs.json._

trait UserJson extends BaseJson{

  implicit val userEmailFormat =
    oneField("email", (s: String) => User.email(s), (u: User.email) => u.value)
  implicit val userIdFormat =
    oneField("value", (s: Long) => User.id(s), (u: User.id) => u.value)
  implicit val userForCreateDtoReads =
    oneField("email", (s: String) => UserForCreateDto(User.email(s)), (u: UserForCreateDto) => u.email.value)

  implicit val userWritesFormat: Format[User] = Json.format[User]

}
