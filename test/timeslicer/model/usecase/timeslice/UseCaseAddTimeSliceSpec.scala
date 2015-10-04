package timeslicer.model.usecase.timeslice
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import timeslicer.model.context.UseCaseContext
import timeslicer.model.project.Project
import timeslicer.model.storage.Storage
import timeslicer.model.user.User
import org.specs2.runner.JUnitRunner
import timeslicer.test.util.TestUtil
import timeslicer.model.user.UserImpl
import timeslicer.model.timeslice.TimeSlice
import timeslicer.model.storage.StorageSuccessResult
import timeslicer.model.storage.StorageFailResult

@RunWith(classOf[JUnitRunner])
class UseCaseAddTimeSliceSpec extends Specification with Mockito {
  /*
   * SETUP
   */
  val mockedUser = mock[User]
  mockedUser.firstName returns "Anders"
  mockedUser.id returns "111111111111"
  val useCaseContext = mock[UseCaseContext]
  useCaseContext.user returns mockedUser

  val interactor = new AddTimeSliceInteractor
  val startdate = "2015-02-01"
  val enddate = "2015-02-28"
  val t1 = TimeSlice("2015-01-31", "10:30", "2015-01-31", "11:00", "Prj1", "Act1", None)
  val request = AddTimeSliceRequestModel(t1)

  val mockedStorage = mock[Storage]
  mockedStorage.addTimeSlice(t1, useCaseContext) returns Right(StorageSuccessResult())
  interactor.storage = mockedStorage
  interactor.log_=(TestUtil.emptyLog)

  /*
   * TEST
   */

  "AddTimeSlice" should {
    "add a timeslice" in {
      interactor
        .execute(request, useCaseContext)
        .success
        .getOrElse(AddTimeSliceResponseModel(false)) must be equalTo (AddTimeSliceResponseModel(true))
    }
  }
  "throw an exception" in {
	  mockedStorage.addTimeSlice(t1, useCaseContext) returns Left(StorageFailResult("failure",Some(new Exception(""))))
    interactor
      .execute(request, useCaseContext)
      .success
      .getOrElse(AddTimeSliceResponseModel(false)) must be equalTo (AddTimeSliceResponseModel(false))
  }
}