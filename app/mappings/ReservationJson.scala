package mappings

import domain.reservation.Reservation
import domain.room.Room
import domain.user.User
import play.api.libs.json._
import play.api.libs.functional.syntax._

trait ReservationJson extends RoomJson with UserJson {
  //TODO: rewrite Jsons
  implicit val reservationIdFormat = Json.format[Reservation.id]
  implicit val reservationPeriodFormat = Json.format[Reservation.period]
  implicit val reservationFormat: Format[Reservation] = (
    (JsPath \ "id").formatNullable[Long] and
      (JsPath \ "roomId").format[Long] and
      (JsPath \ "userId").format[Long] and
      (JsPath \ "period").format[Reservation.period](reservationPeriodFormat)
    ) (
    (id, rId, uId, p) => Reservation(id.map(Reservation.id), Room.id(rId), User.id(uId), p),
    (r: Reservation) => (r.id.map(_.value), r.roomId.value, r.userId.value, r.period)
  )
}
