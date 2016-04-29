package services.book

import domain.reservation.Reservation
import utils.TestData
import domain.room.Room
import org.joda.time.LocalDate
import play.api.test.PlaySpecification


class BookServiceTest extends PlaySpecification with TestData {

  "BookService" should {
    "book room for user for two weeks" in {
      //create user
      await(userService.createUser(user))
      val createdId = await(hotelService.createHotel(hotel))
      await(hotelService.registerRoom(createdId, room))
      val fromDB = await(bookService.book(reservation(LocalDate.now, LocalDate.now.plusWeeks(1))))
      fromDB must equalTo(Some(Reservation.id(1)))
    }
  }
  "BookService" should {
    "cannot book if not free in period" in {
      await(bookService.book(reservation(LocalDate.now.plusDays(5), LocalDate.now.plusWeeks(1)))) must equalTo(None)
    }
  }
  "BookService" should {
    "not found if reservation" in {
      await(hotelService.findAvailableRooms(LocalDate.now.plusDays(5), LocalDate.now.plusWeeks(1), city, roomPrice)) must equalTo(List.empty)
    }
  }
  "BookService" should {
    "found if period after reservation" in {
      val fromDB = await(hotelService.findAvailableRooms(LocalDate.now.plusWeeks(2), LocalDate.now.plusWeeks(3), city, roomPrice))
      val room = Room(Some(roomId1), roomPrice, hotelId)
      fromDB must equalTo(List(room))
    }
  }
}

