package controllers.book

import controllers.{BookController, HotelController, UserController}
import org.junit.Assert._
import org.specs2.mock.Mockito
import play.api.inject.guice._
import play.api.libs.json.Json
import play.api.test.{FakeApplication, FakeRequest, PlaySpecification}
import utils.TestContainer
import play.api.inject.bind
import services.Container

class BookControllerTest extends PlaySpecification with TestContainer with Mockito {
  running(FakeApplication()) {
    "BookController" should {
      "return reservation if can book" in {
        //create user
        val request1 = FakeRequest(POST, "/users")
          .withJsonBody(Json.parse("""{"email":"test"}"""))
        val result1 = route(request1).get
        //create hotel
        val request2 = FakeRequest(POST, "/hotels")
          .withJsonBody(Json.parse("""{"name":"test","city":"test"}"""))
        val result2 = route(request2).get
        //create room
        val request3 = FakeRequest(POST, "/hotels/1/register")
          .withJsonBody(Json.parse("""{"price": 90}"""))
        val result3 = route(request3).get

        assertEquals(200, status(result3))
        contentAsString(result3) mustEqual """{"value":1}"""

        val request = FakeRequest(POST, "/book")
          .withJsonBody(Json.parse("""{"roomId": {"value":1},"userId":{"value":1},"from":"2016-01-01","to":"2016-02-01"}"""))

        val home = route(request).get

        status(home) must equalTo(OK)
        contentAsString(home) must contain("Your new application is ready.")
      }
    }
  }
}
