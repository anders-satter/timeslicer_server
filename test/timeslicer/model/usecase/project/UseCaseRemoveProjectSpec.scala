package timeslicer.model.usecase.project

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import timeslicer.model.storage.Storage
import timeslicer.model.context.UseCaseContext
import timeslicer.test.util.TestUtil
import timeslicer.model.project.Project
import timeslicer.model.storage.StorageSuccessResult
import timeslicer.model.storage.StorageFailResult

@RunWith(classOf[JUnitRunner])
class UseCaseRemoveProjectSpec extends Specification with Mockito {
  /*
   * SETUP
   */
  val storage = mock[Storage]
  val useCaseContext = mock[UseCaseContext]
  useCaseContext.user returns TestUtil.testUser

  val projectToRemove = Project("removeMe", None)
  val request = RemoveProjectRequestModel(projectToRemove)
  val interactor = new RemoveProjectInteractor
  interactor.storage = storage
  //interactor.log_=(TestUtil.emptyLog)
  interactor.log = TestUtil.emptyLog
  

  /*
   * TEST
   */
  "UseCaseRemoveProject" should {

    "successfully remove a project" in {
      storage.removeProject(request.project, useCaseContext) returns Right(new StorageSuccessResult)
      interactor.execute(request, useCaseContext)
        .success
        .getOrElse(RemoveProjectResponseModel(false)) must be equalTo(RemoveProjectResponseModel(true))
    }
    
    "throw an error" in {
      val exception = new Exception("Error")

      storage.removeProject(request.project, useCaseContext) returns
        Left(StorageFailResult("Could not add project", Some(exception)))
      interactor.execute(request, useCaseContext).error.get.failure.get must be equalTo(exception)
    }
  }
}