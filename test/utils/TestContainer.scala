package utils

import domain.hotel.{Hotel, HotelForCreateDto}
import domain.reservation.Reservation
import domain.room.{Room, RoomForRegisterDto}
import domain.user.{User, UserForCreateDto}
import org.joda.time.LocalDate
import services.book.BookService
import services.hotel.HotelService
import services.reservation.ReservationService
import services.room.RoomService
import services.user.UserService
import repositories.implementations.file.hotel.HotelFileRepository
import repositories.implementations.file.reservation.ReservationFileRepository
import repositories.implementations.file.room.RoomFileRepository
import repositories.implementations.file.user.UserFileRepository
import services.Container

import scala.concurrent.{Await, Awaitable}
import scala.concurrent.duration._

trait TestContainer extends Container{
  override val reservationRepository = new ReservationFileRepository
  override val userRepository = new UserFileRepository
  override val roomRepository = new RoomFileRepository
  override val hotelRepository = new HotelFileRepository

  override val reservationService = new ReservationService(reservationRepository)
  override val userService = new UserService(reservationService, userRepository)
  override val roomService = new RoomService(reservationService, roomRepository)
  override val hotelService = new HotelService(roomService, reservationService, hotelRepository)
  override val bookService = new BookService(roomService, reservationService, userService)

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
