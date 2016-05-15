package mappings

import play.api.libs.json.{Format, JsPath}
import play.api.libs.functional.syntax._

trait BaseJson {
  //bug fix, for reading Json with only one field
  def oneField[T, G: Format](name: String, f: G => T, f2: T => G): Format[T] =
    (((JsPath \ name).format[G] and (JsPath \ "nothing").formatNullable[Long])
      ((s, nothing) => f(s), (t: T) => (f2(t), None)))
}
