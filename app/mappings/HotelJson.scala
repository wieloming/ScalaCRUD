package mappings

import domain.hotel.{Hotel, HotelForCreateDto}
import play.api.libs.json._
trait HotelJson {
  implicit val hotelCreateDtoFormat = Json.format[HotelForCreateDto]
  implicit val hotelIdFormat = Json.format[Hotel.id]
  implicit val hotelFormat = Json.format[Hotel]
}
