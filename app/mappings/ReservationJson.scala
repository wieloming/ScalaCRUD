package mappings

import domain.reservation.Reservation
import play.api.libs.json._

trait ReservationJson extends RoomJson{
  implicit val reservationIdFormat = Json.format[Reservation.id]
  implicit val reservationFormat = Json.format[Reservation]
}
