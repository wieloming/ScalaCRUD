package mappings

import domain.hotel.{Hotel, HotelForCreateDto}
import play.api.libs.json._
import play.api.libs.functional.syntax._

trait HotelJson {
  //TODO: rewrite Jsons
  implicit val hotelCreateDtoFormat: Reads[HotelForCreateDto] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "city").read[String]
    ) (
    (n, c) => HotelForCreateDto(Hotel.name(n), c)
  )

  implicit val hotelWrites: Writes[Hotel] = (
    (JsPath \ "id").writeNullable[Long] and
    (JsPath \ "name").write[String] and
    (JsPath \ "city").write[String]
    )((h: Hotel) => (h.id.map(_.value), h.city, h.name.value))
}
