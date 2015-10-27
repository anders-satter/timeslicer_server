package timeslicer.controller.project

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers.GET
import play.api.test.Helpers.OK
import play.api.test.Helpers.defaultAwaitTimeout
import play.api.test.Helpers.route
import play.api.test.Helpers.status
import play.api.test.Helpers.writeableOf_AnyContentAsEmpty
import play.api.test.Helpers._
import play.api.test.WithApplication
import org.specs2.runner.JUnitRunner
import scala.util.Try
import play.api.libs.iteratee.Enumerator
import scala.concurrent.impl.Promise
import scala.concurrent.Promise
import timeslicer.model.autthentication.AuthenticationManager
import timeslicer.controller.util.RequestUtils
import timeslicer.model.autthentication.AuthenticationToken
import timeslicer.model.user.UserImpl

@RunWith(classOf[JUnitRunner])
class ProjectControllerSpec extends Specification {

  /*
   * SETUP
   */

  /*Create a session in session manager storage */
  val authManager = new AuthenticationManager
  
  /**
   * creating a session with an AuthenticationId with only empty string id
   */
  val session = authManager.session(AuthenticationToken("AuthenticationId",""))
  val testUser = new UserImpl
  testUser.id = "111111111111"
  testUser.userName = "anders"
  testUser.email = "abc@se.se"
  testUser.isAuthenticated = true
  session.user = testUser
  

  /*
   * TEST
   */
  "ProjectControllerSpec" should {
    "return projects in json structure" in {

      "return users" in new WithApplication {
        val freq = FakeRequest(GET, "/timeslicer/projects").withSession("AuthenticationId" -> session.id)

        val projects: Future[Result] = route(freq).get
        println(contentAsString(projects))
        ok
      }
    }
  }
}