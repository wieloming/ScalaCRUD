package domain.room

import domain.reservation.Reservation

case class RoomWithReservationsDto(room: Room, reservations: List[Reservation])
