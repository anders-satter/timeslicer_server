package timeslicer.controller.project

import scala.concurrent.Future
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers.GET
import play.api.test.Helpers.contentAsString
import play.api.test.Helpers.defaultAwaitTimeout
import play.api.test.Helpers.route
import play.api.test.Helpers.writeableOf_AnyContentAsEmpty
import play.api.test.WithApplication
import timeslicer.model.authentication.AuthenticationManager
import timeslicer.model.session.SessionManager
import timeslicer.model.user.UserImpl
import scala.util.Success
import scala.util.Failure

@RunWith(classOf[JUnitRunner])
class ProjectControllerSpec extends Specification {

  /*
   * SETUP
   */

  /*Create a session in session manager storage */
  val authManager = new AuthenticationManager

  val testUser = new UserImpl
  testUser.id = "111111111111"
  testUser.userName = "anders"
  testUser.email = "abc@se.se"
  testUser.isAuthenticated = true

  val session = SessionManager.createSession(testUser)
  /*
   * TEST
   */
  "ProjectControllerSpec" should {

    "return projects in json structure" in {

      "return projects1" in new WithApplication {
        println("SESSION.ID" + session.id)

        val freq = FakeRequest(GET, "/timeslicer/projects").withSession("AuthenticationId" -> session.id)
        val projects: Future[Result] = route(freq).get
        contentAsString(projects).contains("Prj1") must beTrue
        val freq2 = FakeRequest(GET, "/timeslicer/projects").withSession("AuthenticationId" -> session.id)
        val projects2: Future[Result] = route(freq2).get
        contentAsString(projects2).contains("Prj1") must beTrue
        val freq3 = FakeRequest(GET, "/timeslicer/projects").withSession("AuthenticationId" -> session.id)
        val projects3: Future[Result] = route(freq3).get
        contentAsString(projects3).contains("Prj1") must beTrue
      }

      "return projects2" in new WithApplication {

        val session2 = SessionManager.createSession(testUser)

        val freq = FakeRequest(GET, "/timeslicer/projects").withSession("AuthenticationId" -> session2.id)
        val projects: Future[Result] = route(freq).get
        contentAsString(projects).contains("Prj1")
        val freq2 = FakeRequest(GET, "/timeslicer/projects").withSession("AuthenticationId" -> session2.id)
        val projects2: Future[Result] = route(freq2).get
        contentAsString(projects2).contains("Prj1") must beTrue
        val freq3 = FakeRequest(GET, "/timeslicer/projects").withSession("AuthenticationId" -> session2.id)
        val projects3: Future[Result] = route(freq3).get
        contentAsString(projects3).contains("Prj1") must beTrue
      }
    }

    "run unauthorized action " in {
      "when authentication id is supplied but wrong" in new WithApplication {

        val freq = FakeRequest(GET, "/timeslicer/projects").withSession("AuthenticationId" -> "xxx")
        val projects: Future[Result] = route(freq).get
        import scala.concurrent.ExecutionContext.Implicits.global
        var resultText = 500
        projects.onComplete({
          case Success(res) => {
            resultText = res.header.status
          }
          case Failure(exception) => println(exception)
        })
        resultText equals 200
      }

      "when AuthenticationId is empty" in new WithApplication {

        val freq = FakeRequest(GET, "/timeslicer/projects").withSession("AuthenticationId" -> "")
        val projects: Future[Result] = route(freq).get
        import scala.concurrent.ExecutionContext.Implicits.global
        var resultText = 500
        projects.onComplete({
          case Success(res) => {
            resultText = res.header.status
          }
          case Failure(exception) => println(exception)
        })
        resultText equals 200
      }
      "when there is no session header" in new WithApplication {

        val freq = FakeRequest(GET, "/timeslicer/projects")
        val projects: Future[Result] = route(freq).get
        import scala.concurrent.ExecutionContext.Implicits.global
        var resultText = 500
        projects.onComplete({
          case Success(res) => {
            resultText = res.header.status
          }
          case Failure(exception) => println(exception)
        })
        resultText equals 200
      }
    }

  }
}