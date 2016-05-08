package API

import org.junit.Assert._
import play.api.libs.json.Json
import play.api.test.{FakeRequest, PlaySpecification}
import utils.TestContainer


class APITest extends PlaySpecification with TestContainer with FakeServer {
  step(server.start())
  "UserController" should {
    "return userId if can create user" in {
      val request = FakeRequest(POST, "/users")
        .withJsonBody(Json.parse("""{"email":"test"}"""))

      val result = route(request).get

      status(result) must equalTo(OK)
      contentAsString(result) mustEqual """1"""
    }
  }

  "HotelController" should {
    "return hotelId if can create hotel" in {
      val request = FakeRequest(POST, "/hotels")
        .withJsonBody(Json.parse("""{"name":"test","city":"test"}"""))

      val result = route(request).get

      assertEquals(200, status(result))
      contentAsString(result) mustEqual """1"""
    }
    "return roomId if can create room" in {
      val request = FakeRequest(POST, "/hotels/1/register")
        .withJsonBody(Json.parse("""{"price": 90}"""))

      val result = route(request).get

      assertEquals(200, status(result))
      contentAsString(result) mustEqual """1"""
    }
    "remove a room" in {
      val request = FakeRequest(DELETE, "/hotels/1/room/1")

      val result = route(request).get

      assertEquals(200, status(result))
      contentAsString(result) mustEqual """{"hotel":{"id":1,"name":"test","city":"test"},"rooms":[]}"""
    }
    "search for available hotel rooms" in {
      val request1 = FakeRequest(POST, "/hotels/1/register")
        .withJsonBody(Json.parse("""{"price": 90}"""))
      route(request1).get
      val request2 = FakeRequest(GET, "/hotels/find/20160101/20170101/test/10")

      val result2 = route(request2).get

      assertEquals(200, status(result2))
      contentAsString(result2) mustEqual """[{"id":2,"price":90,"hotelId":1}]"""
    }
  }
  "BookController" should {
    "return reservationId if can book" in {
      val request = FakeRequest(POST, "/book")
        .withJsonBody(Json.parse("""{"roomId":2,"userId":1,"period":{"from":"2016-01-01","to":"2016-02-01"}}"""))

      val home = route(request).get
      status(home) must equalTo(OK)
      contentAsString(home) mustEqual """1"""
    }
    "return not found if cant book" in {
      val request = FakeRequest(POST, "/book")
        .withJsonBody(Json.parse("""{"roomId":2,"userId":1,"period":{"from":"2016-01-01","to":"2016-02-01"}}"""))

      val home = route(request).get

      status(home) must equalTo(NOT_FOUND)
    }
  }
  "UserController" should {
    "return users reservations" in {
      val request = FakeRequest(GET, "/users/1/reservations")

      val result = route(request).get

      status(result) must equalTo(OK)
      contentAsString(result) mustEqual """[{"id":1,"roomId":2,"userId":1,"period":{"from":"2016-01-01","to":"2016-02-01"}}]"""
    }
  }

  step(server.stop())
}
