package mappings

import domain.user.{User, UserForCreateDto}
import play.api.libs.json._

trait UserJson extends BaseJson {

  implicit val userEmailFormat =
    oneField("email", (s: String) => User.Email(s), (u: User.Email) => u.value)
  implicit val userIdFormat =
    oneField("value", (s: Long) => User.Id(s), (u: User.Id) => u.value)
  implicit val userForCreateDtoReads =
    oneField("email", (s: String) => UserForCreateDto(User.Email(s)), (u: UserForCreateDto) => u.email.value)

  implicit val userWritesFormat: Format[User] = Json.format[User]
}
