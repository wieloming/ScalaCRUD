package utils

import domain.hotel.{Hotel, HotelForCreateDto}
import domain.reservation.Reservation
import domain.room.{Room, RoomForRegisterDto}
import domain.user.{User, UserForCreateDto}
import org.joda.time.LocalDate
import repositories.reservation.ReservationRepository
import services.book.BookService
import services.hotel.HotelService
import services.reservation.ReservationService
import services.room.RoomService
import services.user.UserService
import repositories.hotel.HotelRepository
import repositories.room.RoomRepository
import repositories.user.UserRepository
import scala.concurrent.{Await, Awaitable}
import scala.concurrent.duration._

trait TestData {
  val reservationRepository = new ReservationRepository
  val userRepository = new UserRepository
  val roomRepository = new RoomRepository
  val hotelRepository = new HotelRepository

  val reservationService = new ReservationService(reservationRepository)
  val userService = new UserService(reservationService, userRepository)
  val roomService = new RoomService(reservationService, roomRepository)
  val hotelService = new HotelService(roomService, reservationService, hotelRepository)
  val bookService = new BookService(roomService, reservationService, userService)

  val hotelId = Hotel.id(1L)
  val roomId1 = Room.id(1L)
  val roomId2 = Room.id(2L)
  val userId = User.id(1L)

  val roomPrice = 20
  val city = "city"

  val room = RoomForRegisterDto(roomPrice)
  val hotel = HotelForCreateDto("name", city)
  val user = UserForCreateDto("foo@test.com")
  def reservation(from: LocalDate, to: LocalDate) = Reservation(None, roomId1, userId, from, to)

  val finiteDuration = 10 seconds
  def await[T](f: Awaitable[T]): T = Await.result(f, finiteDuration)
}
