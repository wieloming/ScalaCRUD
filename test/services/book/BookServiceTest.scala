package services.book

import domain.reservation.Reservation
import utils.TestContainer
import domain.room.Room
import org.joda.time.LocalDate
import play.api.test.PlaySpecification


class BookServiceTest extends PlaySpecification with TestContainer {

  "BookService" should {
    "book room for user for two weeks" in {
      //create user
      await(userService.createUser(user))
      val createdId = await(hotelService.createHotel(hotel))
      await(hotelService.registerRoom(createdId, room))
      val fromDB = await(bookService.book(reservation(oneWeekPeriod)))
      fromDB must equalTo(Some(Reservation.ModelId(1)))
    }
  }
  "BookService" should {
    "cannot book if not free in period" in {
      val period = Reservation.Period(LocalDate.now.plusDays(5), LocalDate.now.plusWeeks(1))
      await(bookService.book(reservation(period))) must equalTo(None)
    }
  }
  "BookService" should {
    "not found if reservation" in {
      val period = Reservation.Period(LocalDate.now.plusDays(5), LocalDate.now.plusWeeks(1))
      await(hotelService.findAvailableRooms(period, city, roomPrice)) must equalTo(List.empty)
    }
  }
  "BookService" should {
    "found if period after reservation" in {
      val period = Reservation.Period(LocalDate.now.plusWeeks(2), LocalDate.now.plusWeeks(3))
      val fromDB = await(hotelService.findAvailableRooms(period, city, roomPrice))
      val room = Room(Some(roomId1), roomPrice, hotelId)
      fromDB must equalTo(List(room))
    }
  }
}

