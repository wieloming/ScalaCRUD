package API

import play.api.test._

trait FakeServer {

  implicit val app: FakeApplication = FakeApplication()
  implicit def port: Port = Helpers.testServerPort

  val server = TestServer(port, app)
}