package controllers.hotel

import org.junit.Assert._
import play.api.libs.json.Json
import play.api.test.{FakeApplication, FakeRequest, PlaySpecification}
import utils.TestContainer


class HotelControllerTest extends PlaySpecification with TestContainer {
  running(FakeApplication()) {

    "HotelController" should {
      "return hotelId if can create hotel" in {
        val request = FakeRequest(POST, "/hotels")
          .withJsonBody(Json.parse("""{"name":"test","city":"test"}"""))

        val result = route(request).get

        assertEquals(200, status(result))
        contentAsString(result) mustEqual """{"value":1}"""
      }
      "return roomId if can create room" in {
        val request = FakeRequest(POST, "/hotels/1/register")
          .withJsonBody(Json.parse("""{"price": 90}"""))

        val result = route(request).get

        assertEquals(200, status(result))
        contentAsString(result) mustEqual """{"value":1}"""
      }
      "remove a room" in {
        val request = FakeRequest(DELETE, "/hotels/1/room/1")

        val result = route(request).get

        assertEquals(200, status(result))
        contentAsString(result) mustEqual """{"hotel":{"id":{"value":1},"name":"test","city":"test"},"rooms":[]}"""
      }
      "search for available hotel rooms" in {
        /////create new room
        val request1 = FakeRequest(POST, "/hotels/1/register")
          .withJsonBody(Json.parse("""{"price": 90}"""))
        val result = route(request1).get
        ////////////////////
        val request2 = FakeRequest(GET, "/hotels/find/20160101/20170101/test/10")

        val result2 = route(request2).get

        assertEquals(200, status(result2))
        contentAsString(result2) mustEqual """[{"id":{"value":2},"price":90,"hotelId":{"value":1}}]"""
      }
    }
  }
}
