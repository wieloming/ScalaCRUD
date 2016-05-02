package mappings

import domain.reservation.Reservation
import play.api.libs.json._

trait ReservationJson extends RoomJson with UserJson{
  implicit val reservationIdFormat = Json.format[Reservation.id]
  implicit val reservationPeriodFormat = Json.format[Reservation.period]
  implicit val reservationFormat = Json.format[Reservation]
}
