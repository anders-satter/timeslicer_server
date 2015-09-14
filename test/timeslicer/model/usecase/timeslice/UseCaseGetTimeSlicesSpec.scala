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

@RunWith(classOf[JUnitRunner])
class UseCaseGetTimeSlicesSpec extends Specification with Mockito {

  val mockedUser = mock[User]
  mockedUser.firstName returns "Anders"
  mockedUser.id returns "111111111111"
  val useCaseContext = mock[UseCaseContext]
  useCaseContext.user returns mockedUser 
  
  val interactor = new GetTimeSlicesInteractor
  val startdate = "2015-02-01"
  val enddate = "2015-02-28"
  val request = GetTimeSlicesRequestModel(startdate,enddate)
  
  val t1 = TimeSlice("2015-01-31", "10:30","2015-01-31", "11:00", "Prj1", "Act1", None)
  val t2 = TimeSlice("2015-02-01", "08:14","2015-02-01", "10:00", "Prj1", "Act1", None)
  val t3 = TimeSlice("2015-02-15", "10:30","2015-02-15", "11:00", "Prj1", "Act1", None)
  
  val mockedStorage = mock[Storage]
  mockedStorage.timeslices(startdate, enddate, useCaseContext) returns Option(Seq(t1, t2, t3))
  interactor.storage = mockedStorage
  interactor.log_=(TestUtil.emptyLog) 
  
  "GetTimeSlicesInteractor" should {
    "return timeslices" in {
      interactor.execute(request,useCaseContext).success.get.timeslices.length > 0 must beTrue      
    }
  }
}