package mappings

import domain.hotel.HotelWithRoomsDto
import play.api.libs.functional.syntax._
import domain.room.{Room, RoomForRegisterDto}
import play.api.libs.json._

trait RoomJson extends UserJson with HotelJson{
  //TODO: rewrite Jsons
  //bug fix, for reading Json with only one field
  implicit val RoomForRegisterDtoFormat: Reads[RoomForRegisterDto] =
    ((JsPath \ "price").read[Long] and
      (JsPath \ "nothing").readNullable[Long])((p, nothing) => RoomForRegisterDto(Room.price(p)))

  implicit val RoomIdFormat = Json.format[Room.id]
  implicit val roomWrites: Writes[Room] = (
    (JsPath \ "id").writeNullable[Long] and
      (JsPath \ "price").write[Long] and
      (JsPath \ "hotelId").write[Long]
    )((r: Room) => (r.id.map(_.value), r.price.value, r.hotelId.value))
  implicit val hotelWithRoomsWrite = Json.writes[HotelWithRoomsDto]
}
