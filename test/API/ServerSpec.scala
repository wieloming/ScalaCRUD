package API

import play.api.test._

trait ServerSpec {

  implicit val app: FakeApplication = FakeApplication()
  implicit def port: Port = Helpers.testServerPort

  val server = TestServer(port, app)
}