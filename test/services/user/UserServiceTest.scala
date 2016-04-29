package services.user

import domain.reservation.Reservation
import domain.user.User
import org.joda.time.LocalDate
import play.api.test.PlaySpecification
import utils.TestData

class UserServiceTest extends PlaySpecification with TestData {
  "UserService" should {
    "return userId if can create user" in {
      val created = await(userService.createUser(user))
      userId must equalTo(created)
    }
    "return user from DB after creating" in {
      await(userService.findById(userId)).get must equalTo(User(Some(userId), "foo@test.com"))
    }
  }
  "UserService" should {
    "find users reservations" in {
      //create hotel
      val hotelId = await(hotelService.createHotel(hotel))
      //create room
      await(hotelService.registerRoom(hotelId, room))
      //book room
      val reservationId = await(bookService.book(reservation(LocalDate.now, LocalDate.now.plusWeeks(1))))
      await(userService.findReservations(userId)) must equalTo(List(Reservation(reservationId, roomId1, userId, LocalDate.now, LocalDate.now.plusWeeks(1))))
    }
  }
}
