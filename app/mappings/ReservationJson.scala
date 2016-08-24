package mappings

import domain.reservation.Reservation
import domain.room.Room
import domain.user.User
import play.api.libs.json._
import play.api.libs.functional.syntax._

trait ReservationJson extends RoomJson with UserJson {
  implicit val reservationIdFormat = Json.format[Reservation.ModelId]
  implicit val reservationPeriodFormat = Json.format[Reservation.Period]
  implicit val reservationFormat: Format[Reservation] = (
    (JsPath \ "id").formatNullable[Long] and
      (JsPath \ "roomId").format[Long] and
      (JsPath \ "userId").format[Long] and
      (JsPath \ "period").format[Reservation.Period](reservationPeriodFormat)
    ) (
    (id, rId, uId, p) => Reservation(id.map(Reservation.ModelId), Room.ModelId(rId), User.ModelId(uId), p),
    (r: Reservation) => (r.id.map(_.value), r.roomId.value, r.userId.value, r.period)
  )
}
