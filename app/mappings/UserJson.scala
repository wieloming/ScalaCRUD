package mappings

import domain.user.{User, UserForCreateDto}
import play.api.libs.json._

trait UserJson {
  implicit val userForCreateDtoFormat = Json.format[UserForCreateDto]
  implicit val userIdFormat = Json.format[User.id]
  implicit val userFormat = Json.format[User]
}
