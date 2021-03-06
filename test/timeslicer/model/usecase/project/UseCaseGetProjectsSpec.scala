package timeslicer.model.usecase.project

import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import timeslicer.model.context.UseCaseContext
import timeslicer.model.project.Project
import timeslicer.model.storage.Storage
import timeslicer.model.user.User
import org.specs2.runner.JUnitRunner
import timeslicer.test.util.TestUtil

@RunWith(classOf[JUnitRunner])
class UseCaseGetProjectsSpec extends Specification with Mockito {
  /*
   * SETUP
   */
  
  val storage = mock[Storage]
  val useCaseContext = mock[UseCaseContext]
  /* create a project list of 2 projects*/
  storage.projects(useCaseContext) returns Option(List(Project("Project1", null), Project("Project2", null)))
  val requestModel = GetProjectsRequestModel();
 
  val interactor = new GetProjectsInteractor
  interactor.log_=(TestUtil.emptyLog) 
  
  interactor.storage = storage
  useCaseContext.user returns TestUtil.testUser

  /*
   * TEST
   */

  "ProjectListInteractor" should {

    "return a ProjectListResponseModel" in {
      val res = interactor.execute(requestModel, useCaseContext).success.map(x => {
        val list = x.projectList
        list
      })
      res.getOrElse(List()).length == 2 must beTrue
    }
  }
}