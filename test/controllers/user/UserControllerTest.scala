package controllers.user

import play.api.libs.json.Json
import play.api.test.{FakeApplication, FakeRequest, PlaySpecification}
import utils.TestContainer


class UserControllerTest extends PlaySpecification with TestContainer {

  running(FakeApplication()) {
    "UserController" should {
      "return userId if can create user" in {
        val request = FakeRequest(POST, "/users")
          .withJsonBody(Json.parse("""{"email":"test"}"""))

        val result = route(request).get

        status(result) must equalTo(OK)
        contentAsString(result) mustEqual """{"value":1}"""
      }
    }
  }
}
