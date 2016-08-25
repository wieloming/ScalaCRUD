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
    (JsPath \ "id").format[Long] and
      (JsPath \ "roomId").format[Long] and
      (JsPath \ "userId").format[Long] and
      (JsPath \ "period").format[Reservation.Period](reservationPeriodFormat)
    ) (
    (id, rId, uId, p) => Reservation(Reservation.ModelId(id), Room.ModelId(rId), User.ModelId(uId), p),
    (r: Reservation) => (r.id.value, r.roomId.value, r.userId.value, r.period)
  )
 implicit val reservationForCreateFormat: Format[Reservation.ForCreate] = (
      (JsPath \ "roomId").format[Long] and
      (JsPath \ "userId").format[Long] and
      (JsPath \ "period").format[Reservation.Period](reservationPeriodFormat)
    ) (
    (rId, uId, p) => Reservation.ForCreate(Room.ModelId(rId), User.ModelId(uId), p),
    (r: Reservation.ForCreate) => (r.roomId.value, r.userId.value, r.period)
  )
}
