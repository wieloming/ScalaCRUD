package mappings

import play.api.libs.json.{Json, Writes}
import repositories.interfaces.Errors

trait UtilsJson {

  implicit val errorsWrites: Writes[Errors] = Json.format[Errors]

}
