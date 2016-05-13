package mappings

import domain.user.{User, UserForCreateDto}
import play.api.libs.json._
import play.api.libs.functional.syntax._

trait UserJson {
  //TODO: rewrite Jsons
  //bug fix, for reading Json with only one field
  implicit val userForCreateDtoReads: Reads[UserForCreateDto] =
    ((JsPath \ "email").read[String] and
      (JsPath \ "nothing").readNullable[Long])((s, nothing) => UserForCreateDto(User.email(s)))
  implicit val userIdFormat = Json.format[User.id]
  implicit val userWrites: Writes[User] = (
    (JsPath \ "id").writeNullable[Long] and
      (JsPath \ "email").write[String]
    )((u: User) => (u.id.map(_.value), u.email.value))}
