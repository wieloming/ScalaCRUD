package services

import javax.inject.Singleton

import repositories.implementations.inMemory.hotel.HotelInMemoryRepository
import repositories.implementations.inMemory.reservation.ReservationInMemoryRepository
import repositories.implementations.inMemory.room.RoomInMemoryRepository
import repositories.implementations.inMemory.user.UserInMemoryRepository
import services.book.BookService
import services.hotel.HotelService
import services.reservation.ReservationService
import services.room.RoomService
import services.user.UserService

@Singleton
class Container {
  val reservationRepository = new ReservationInMemoryRepository
  val userRepository = new UserInMemoryRepository
  val roomRepository = new RoomInMemoryRepository
  val hotelRepository = new HotelInMemoryRepository

  val reservationService = new ReservationService(reservationRepository)
  val userService = new UserService(reservationService, userRepository)
  val roomService = new RoomService(reservationService, roomRepository)
  val hotelService = new HotelService(roomService, reservationService, hotelRepository)
  val bookService = new BookService(roomService, reservationService, userService)
}
