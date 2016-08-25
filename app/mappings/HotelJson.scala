package mappings

import domain.hotel.Hotel
import play.api.libs.json._
import play.api.libs.functional.syntax._

trait HotelJson {
  implicit val hotelForCreateFormat: Reads[Hotel.ForCreate] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "city").read[String]
    ) (
    (n, c) => Hotel.ForCreate(Hotel.Name(n), Hotel.City(c))
  )

  implicit val hotelWrites: Writes[Hotel] = (
    (JsPath \ "id").write[Long] and
      (JsPath \ "name").write[String] and
      (JsPath \ "city").write[String]
    ) ((h: Hotel) => (h.id.value, h.city.value, h.name.value))
}
