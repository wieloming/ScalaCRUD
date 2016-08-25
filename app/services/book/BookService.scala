package services.book

import domain.reservation.Reservation
import services.reservation.ReservationService
import services.room.RoomService
import services.user.UserService
import utils.ValueOrErrors

class BookService(roomService: RoomService, reservationService: ReservationService, userService: UserService) {

  def book(reservation: Reservation.ForCreate): ValueOrErrors[Reservation.ModelId] = {
    for {
      user <- userService.findById(reservation.userId)
      room <- roomService.findById(reservation.roomId)
      isFree <- roomService.isFreeBetween(reservation.roomId, reservation.period)
      reservation <- reservationService.create(reservation)
    } yield reservation
  }
}
