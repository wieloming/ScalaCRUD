package services

import javax.inject.Singleton

import repositories.implementations.file.hotel.HotelFileRepository
import repositories.implementations.file.reservation.ReservationFileRepository
import repositories.implementations.file.room.RoomFileRepository
import repositories.implementations.file.user.UserFileRepository
import services.book.BookService
import services.hotel.HotelService
import services.reservation.ReservationService
import services.room.RoomService
import services.user.UserService

@Singleton
class Container {
  val reservationRepository = new ReservationFileRepository
  val userRepository = new UserFileRepository
  val roomRepository = new RoomFileRepository
  val hotelRepository = new HotelFileRepository

  val reservationService = new ReservationService(reservationRepository)
  val userService = new UserService(reservationService, userRepository)
  val roomService = new RoomService(reservationService, roomRepository)
  val hotelService = new HotelService(roomService, reservationService, hotelRepository)
  val bookService = new BookService(roomService, reservationService, userService)
}
