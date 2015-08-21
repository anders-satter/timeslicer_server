package timeslicer.model.usecase.project

import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import timeslicer.model.context.UseCaseContext
import timeslicer.model.project.Project
import timeslicer.model.storage.Storage
import timeslicer.model.user.User
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UseCaseGetProjectsSpec extends Specification with Mockito {
  /*
   * SETUP
   */
  val storage = mock[Storage]
  val useCaseContext = mock[UseCaseContext]
  /* create a project list of 2 projects*/
  storage.projects(useCaseContext) returns Option(List(Project("Project1", null), Project("Project2", null)))
  val requestModel = GetProjectsRequestModel(storage);
  val interactor = new GetProjectsInteractor
  useCaseContext.user returns new User {
    override def firstName = "Anders"
    override def lastName = "Sätter"
    override def id = "111111111111"
    override def isAuthorized = true
    override def isAuthenticated = true
    override def email = None
  }
  /*
   * TEST
   */

  "ProjectListInteractor" should {

    "return a ProjectListResponseModel" in {
      interactor.execute(requestModel, useCaseContext)
        .projectList != null must beTrue
      interactor.execute(requestModel, useCaseContext)
        .projectList.length > 0 must beTrue
    }
  }
}