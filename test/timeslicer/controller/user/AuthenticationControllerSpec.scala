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
import timeslicer.model.user.PasswordUtil
import play.api.libs.json.Json

@RunWith(classOf[JUnitRunner])
class AuthenticationControllerSpec extends Specification with Mockito {
  /*
   * SETUP
   */

  /*
   * TEST
   */

  "AuthenticationControllerSpec" should {

    /*
     * Don't we need to read the session header 
     */
    
    "return authenticated user" in new WithApplication {

      val body = Json.parse(""" {"userName": "bentson", "email":"abc@def.se", "password":"bluepot01"} """);
      val fakeRequest = FakeRequest(POST, "/timeslicer/authentication/user")
        .withHeaders((HeaderNames.CONTENT_TYPE, "application/json"))
        .withJsonBody(body)
        .withSession(("session", "87ofltrfd5onv5sl062ot"))

      val fakeRequest2 = fakeRequest.withSession()
      val result: Future[Result] = route(fakeRequest).get
      //println("this is the value")
      //println(contentAsString(result).toString())
      status(result) must equalTo(OK)
    }

    "return failure for wrong password" in {
      val s: Seq[(String, String)] = Seq((HeaderNames.CONTENT_TYPE, "application/json"))
      "return ok" in new WithApplication {
        val fakeRequest = FakeRequest(POST, "/timeslicer/authentication/user",
          FakeHeaders(s), """ {"userName": "bentson", "email":"abc@def.se", "password":"wrongpassword"} """)
        val result: Future[Result] = route(fakeRequest).get
        //contentAsString(result).contains("Hello") must beTrue     
        status(result) must equalTo(UNAUTHORIZED)
      }
    }

    "return failure for non-existing user name" in {
      val s: Seq[(String, String)] = Seq((HeaderNames.CONTENT_TYPE, "application/json"))
      "return ok" in new WithApplication {
        val fakeRequest = FakeRequest(POST, "/timeslicer/authentication/user",
          FakeHeaders(s), """ {"userName": "nonexistent", "email":"noemail@nowhere.com", "password":"bluepot01"} """)
        val result: Future[Result] = route(fakeRequest).get
        //contentAsString(result).contains("Hello") must beTrue     
        status(result) must equalTo(UNAUTHORIZED)

      }
    }
  }
}