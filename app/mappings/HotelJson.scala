package mappings

import domain.hotel.{Hotel, HotelForCreateDto}
import play.api.libs.json._
import play.api.libs.functional.syntax._

trait HotelJson {
  implicit val hotelCreateDtoFormat = Json.format[HotelForCreateDto]
  implicit val hotelIdFormat = Json.format[Hotel.id]
  implicit val hotelWrites: Writes[Hotel] = (
    (JsPath \ "id").writeNullable[Long] and
    (JsPath \ "name").write[String] and
    (JsPath \ "city").write[String]
    )((h: Hotel) => (h.id.map(_.value), h.city, h.name))
}
