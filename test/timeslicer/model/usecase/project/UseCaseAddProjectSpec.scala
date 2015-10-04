package timeslicer.model.usecase.project

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import timeslicer.model.project.Project
import timeslicer.model.context.UseCaseContext
import timeslicer.model.storage.Storage
import timeslicer.test.util.TestUtil
import timeslicer.model.user.User
import timeslicer.model.storage.StorageSuccessResult
import timeslicer.model.storage.StorageFailResult
import timeslicer.model.framework.ErrorContainer
import scala.util.Failure

@RunWith(classOf[JUnitRunner])
class UseCaseAddProjectSpec extends Specification with Mockito {
  /*
   * SETUP
   */

  val storage = mock[Storage]
  val useCaseContext = mock[UseCaseContext]
  useCaseContext.user returns TestUtil.testUser 
    

  val interactor = new AddProjectInteractor
  interactor.storage = storage
  interactor.log_=(TestUtil.emptyLog)

  val projectToAdd = Project("ProjectToAdd", None)
  val request = AddProjectRequestModel(projectToAdd)

  /*
   * TEST
   */
  "AddProjectInteractor" should {
    "add project" in {
      storage.addProject(request.project, useCaseContext) returns Right(new StorageSuccessResult)

      interactor
        .execute(request, useCaseContext)
        .success
        .getOrElse(AddProjectResponseModel(false)) must be equalTo (AddProjectResponseModel(true))
    }

    "not add project in" in {
      val exception = new Exception("Error")

      storage.addProject(request.project, useCaseContext) returns
        Left(StorageFailResult("Could not add project", Some(exception)))

      val result = interactor.execute(request, useCaseContext)
      result.error.get.failure.get must be equalTo (exception)
    }
  }

}