package mappings

import domain.user.{User, UserForCreateDto}
import play.api.libs.json._
import play.api.libs.functional.syntax._

trait UserJson {
  implicit val userForCreateDtoFormat = Json.format[UserForCreateDto]
  implicit val userIdFormat = Json.format[User.id]
  implicit val userWrites: Writes[User] = (
    (JsPath \ "id").writeNullable[Long] and
      (JsPath \ "email").write[String]
    )((u: User) => (u.id.map(_.value), u.email))}
