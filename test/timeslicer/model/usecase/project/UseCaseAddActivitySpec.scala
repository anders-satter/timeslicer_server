package timeslicer.model.usecase.project

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import timeslicer.model.storage.Storage
import timeslicer.model.context.UseCaseContext
import timeslicer.test.util.TestUtil
import timeslicer.model.project.Activity
import timeslicer.model.project.Project
import timeslicer.model.storage.StorageSuccessResult
import timeslicer.model.storage.StorageFailResult

@RunWith(classOf[JUnitRunner])
class UseCaseAddActivitySpec extends Specification with Mockito {
  /*
   * SETUP
   */
  val storage = mock[Storage]
  val useCaseContext = mock[UseCaseContext]
  useCaseContext.user returns TestUtil.testUser

  val activityToAdd = Activity("act1")
  val project = Project("Prj1", None)

  val request = AddActivityRequestModel(project, activityToAdd)
  //val request = AddActivityRequestModel(null, null)

  val interactor = new AddActivityInteractor
  interactor.storage = storage
  interactor.log = TestUtil.emptyLog

  "AddActivity" should {
    "add an activity" in {
      storage.addActivity(request.project, request.activity, useCaseContext) returns Right(new StorageSuccessResult)

      interactor
        .execute(request, useCaseContext)
        .success
        .getOrElse(AddActivityResponseModel(false)) must be equalTo (AddActivityResponseModel(true))

    }

    "not add activity in" in {
      val exception = new Exception("Error")

      storage.addActivity(request.project, request.activity, useCaseContext) returns
        Left(StorageFailResult("Could not add project", Some(exception)))

      val result = interactor.execute(request, useCaseContext)
      result.error.get.failure.get must be equalTo (exception)
    }

  }

}