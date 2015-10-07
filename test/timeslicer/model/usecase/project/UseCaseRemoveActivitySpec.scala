package timeslicer.model.usecase.project

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import timeslicer.model.storage.Storage
import timeslicer.model.context.UseCaseContext
import timeslicer.test.util.TestUtil
import timeslicer.model.project.Project
import timeslicer.model.project.Activity
import timeslicer.model.storage.StorageSuccessResult
import timeslicer.model.storage.StorageFailResult

@RunWith(classOf[JUnitRunner])
class UseCaseRemoveActivitySpec extends Specification with Mockito {
  /*
   * SETUP
   */
  val storage = mock[Storage]
  val useCaseContext = mock[UseCaseContext]
  useCaseContext.user returns TestUtil.testUser
  val project = Project("Prj1", None)
  val activity = Activity("act1")    
  val request = RemoveActivityRequestModel(project, activity)
  
  val interactor = new RemoveActivityInteractor
  interactor.storage = storage
  interactor.log = TestUtil.emptyLog
  
  "UseCaseRemoveActivity" should {

    "successfully remove an activity" in {
      storage.removeActivity(request.project,request.activity, useCaseContext) returns Right(new StorageSuccessResult)
      interactor.execute(request, useCaseContext)
        .success
        .getOrElse(RemoveProjectResponseModel(false)) must be equalTo(RemoveActivityResponseModel(true))
    }
    
    "throw an error" in {
      val exception = new Exception("Error")

      storage.removeActivity(request.project,request.activity, useCaseContext) returns
        Left(StorageFailResult("Could not remove activity", Some(exception)))
      
      interactor.execute(request, useCaseContext).error.get.failure.get must be equalTo(exception)
    }
  }
  
  

}