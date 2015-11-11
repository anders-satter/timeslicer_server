package timeslicer.controller.user

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito
import play.api.test.FakeRequest
import play.api.test.Helpers.POST
import play.api.test.FakeHeaders
import scala.concurrent.Future
import play.api.mvc.Result
import play.api.test.Helpers.route
import play.api.test.Helpers._
import play.api.test.WithApplication
import play.api.http.HeaderNames

@RunWith(classOf[JUnitRunner])
class AuthenticationControllerSpec extends Specification with Mockito {
  /*
   * SETUP
   * 
   * The authentication controller needs a user name and a 
   * password
   * 
   */

  /*
   * TEST
   */

  "AuthenticationControllerSpec" should {
    "authenticate" in {
      val s: Seq[(String, String)] = Seq((HeaderNames.CONTENT_TYPE, "application/json"))
      "return ok" in new WithApplication {
        val fakeRequest = FakeRequest(POST, "/timeslicer/authentication/user",
          FakeHeaders(s), """ {"userName": "Bent", "email":"abc@def.se", "password":"bluepot01"} """)
        val result: Future[Result] = route(fakeRequest).get
        contentAsString(result).contains("Hello") must beTrue

        
      }
    }
  }
}