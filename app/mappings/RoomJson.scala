package mappings

import domain.hotel.HotelWithRoomsDto
import play.api.libs.functional.syntax._
import domain.room.{Room, RoomForRegisterDto}
import play.api.libs.json._

trait RoomJson extends UserJson with HotelJson{

  implicit val RoomForRegisterDtoFormat =
    oneField("price", (s: Long) => RoomForRegisterDto(Room.price(s)), (u: RoomForRegisterDto) => u.price.value)

  implicit val roomWrites: Writes[Room] = (
    (JsPath \ "id").writeNullable[Long] and
      (JsPath \ "price").write[Long] and
      (JsPath \ "hotelId").write[Long]
    )((r: Room) => (r.id.map(_.value), r.price.value, r.hotelId.value))

  implicit val hotelWithRoomsWrite = Json.writes[HotelWithRoomsDto]
}
