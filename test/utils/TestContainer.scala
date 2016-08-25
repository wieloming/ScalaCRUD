package utils

import domain.hotel.{Hotel, HotelForCreateDto}
import domain.reservation.Reservation
import domain.room.{Room, RoomForRegisterDto}
import domain.user.User
import org.joda.time.LocalDate
import services.book.BookService
import services.hotel.HotelService
import services.reservation.ReservationService
import services.room.RoomService
import services.user.UserService
import repositories.implementations.inMemory.hotel.HotelInMemoryRepository
import repositories.implementations.inMemory.reservation.ReservationInMemoryRepository
import repositories.implementations.inMemory.room.RoomInMemoryRepository
import repositories.implementations.inMemory.user.UserInMemoryRepository

import scala.concurrent.{Await, Awaitable}
import scala.concurrent.duration._

trait TestContainer {
  val reservationRepository = new ReservationInMemoryRepository
  val userRepository = new UserInMemoryRepository
  val roomRepository = new RoomInMemoryRepository
  val hotelRepository = new HotelInMemoryRepository

  val reservationService = new ReservationService(reservationRepository)
  val userService = new UserService(reservationService, userRepository)
  val roomService = new RoomService(reservationService, roomRepository)
  val hotelService = new HotelService(roomService, reservationService, hotelRepository)
  val bookService = new BookService(roomService, reservationService, userService)

  val hotelId = Hotel.ModelId(1L)
  val roomId1 = Room.ModelId(1L)
  val roomId2 = Room.ModelId(2L)
  val userId = User.ModelId(1L)

  val hotelName = Hotel.Name("name")
  val roomPrice = Room.Price(20)
  val city = Hotel.City("city")

  val room = RoomForRegisterDto(roomPrice)
  val hotel = HotelForCreateDto(Hotel.Name("name"), city)
  val user = UserForCreateDto(User.Email("foo@test.com"))
  val oneWeekPeriod = Reservation.Period(LocalDate.now, LocalDate.now.plusWeeks(1))
  def reservation(period: Reservation.Period) = Reservation(None, roomId1, userId, period)

  val finiteDuration = 10 seconds
  def await[T](f: Awaitable[T]): T = Await.result(f, finiteDuration)
}
