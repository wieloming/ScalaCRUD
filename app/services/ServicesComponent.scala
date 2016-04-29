package services

import repositories.hotel.HotelRepository
import repositories.reservation.ReservationRepository
import repositories.room.RoomRepository
import repositories.user.UserRepository
import services.book.BookService
import services.hotel.HotelService
import services.reservation.ReservationService
import services.room.RoomService
import services.user.UserService

trait ServicesComponent {
  val reservationRepository = new ReservationRepository
  val userRepository = new UserRepository
  val roomRepository = new RoomRepository
  val hotelRepository = new HotelRepository

  val reservationService = new ReservationService(reservationRepository)
  val userService = new UserService(reservationService, userRepository)
  val roomService = new RoomService(reservationService, roomRepository)
  val hotelService = new HotelService(roomService, reservationService, hotelRepository)
  val bookService = new BookService(roomService, reservationService, userService)
}
