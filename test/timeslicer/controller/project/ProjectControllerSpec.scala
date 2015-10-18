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

@RunWith(classOf[JUnitRunner])
class ProjectControllerSpec extends Specification {

  /*
   * TEST
   */
  "ProjectControllerSpec" should {
    "return projects in json structure" in {
     
      
      "return users" in new WithApplication {
        val projects: Future[Result] = route(FakeRequest(GET, "/projects")).get
        
        println(contentAsString(projects))
      }
    }
  }
}