package domain.reservation

import domain.room.Room
import domain.user.User
import org.joda.time.LocalDate

case class Reservation(id: Option[Reservation.Id], roomId: Room.Id, userId: User.Id, period: Reservation.Period) {
  def notIn(p: Reservation.Period): Boolean = {
    val bothBefore = (period.from isBefore p.from) && (period.to isBefore p.to)
    val bothAfter = (period.from isAfter p.from) && (period.to isAfter p.to)
    bothBefore || bothAfter
  }
}
case object Reservation {
  case class Id(value: Long) extends domain.Id
  case class Period(from: LocalDate, to: LocalDate){
    require(from isBefore to)
  }
}


