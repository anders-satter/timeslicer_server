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

    "test /timeslicer/authentication/user" in {

      "return authenticated user" in new WithApplication {

        val body = Json.parse(""" {"userName": "bentson", "email":"abc@def.se", "password":"bluepot01"} """);
        val fakeRequest = FakeRequest(POST, "/timeslicer/authentication/user")
          .withHeaders((HeaderNames.CONTENT_TYPE, "application/json"))
          .withJsonBody(body)
          .withSession(("session", "87ofltrfd5onv5sl062ot"))

        val fakeRequest2 = fakeRequest.withSession()
        val result: Future[Result] = route(fakeRequest).get
        status(result) must equalTo(OK)
      }

      "return failure for wrong password" in {
        val s: Seq[(String, String)] = Seq((HeaderNames.CONTENT_TYPE, "application/json"))
        "return ok" in new WithApplication {
          val fakeRequest = FakeRequest(POST, "/timeslicer/authentication/user",
            FakeHeaders(s), """ {"userName": "bentson", "email":"abc@def.se", "password":"wrongpassword"} """)
          val result: Future[Result] = route(fakeRequest).get
          status(result) must equalTo(UNAUTHORIZED)
        }
      }

      "return failure for non-existing user name" in {
        val s: Seq[(String, String)] = Seq((HeaderNames.CONTENT_TYPE, "application/json"))
        "return ok" in new WithApplication {
          val fakeRequest = FakeRequest(POST, "/timeslicer/authentication/user",
            FakeHeaders(s), """ {"userName": "nonexistent", "email":"noemail@nowhere.com", "password":"bluepot01"} """)
          val result: Future[Result] = route(fakeRequest).get
          status(result) must equalTo(UNAUTHORIZED)

        }
      }
    }

    "test /timeslicer/authentication/login" in {

      "return ok when the user is found" in new WithApplication {

        val body = Json.parse(""" {"userName": "bentson", "email":"abc@def.se", "password":"bluepot01"} """);
        val fakeRequest = FakeRequest(POST, "/timeslicer/authentication/login")
          .withHeaders((HeaderNames.CONTENT_TYPE, "application/json"))
          .withJsonBody(body)

        val result: Future[Result] = route(fakeRequest).get
        status(result) must equalTo(OK)
        contentAsString(result).contains("User created")
        session(result).data.get("AuthenticationId").get.length > 10 must beTrue
      }

      "return unauthorized when the user is not found" in new WithApplication {
    	  
    	  val body = Json.parse(""" {"userName": "Nouseratall", "email":"no@email.se", "password":"bluepot01"} """);
    	  val fakeRequest = FakeRequest(POST, "/timeslicer/authentication/login")
    			  .withHeaders((HeaderNames.CONTENT_TYPE, "application/json"))
    			  .withJsonBody(body)    			  
    			  val result: Future[Result] = route(fakeRequest).get
    			  status(result) must equalTo(UNAUTHORIZED)
      }
    }
  }
}