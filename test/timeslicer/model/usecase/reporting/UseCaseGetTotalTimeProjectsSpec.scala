package timeslicer.model.usecase.reporting

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import timeslicer.model.user.User
import timeslicer.model.context.UseCaseContext
import timeslicer.model.storage.filestorage.FileStorage
import timeslicer.test.util.TestUtil

@RunWith(classOf[JUnitRunner])
class UseCaseGetTotalTimeProjectsSpec extends Specification with Mockito{
  /*
   * SETUP
   */
  
  val testFileStorageBasePath = "test/filestorage/data"
  val testPrjFileName = "prj.txt"
  val testLogFileName = "log.txt"
  val testUsersFileName = "users.json"
  val fileStorage = new FileStorage(testFileStorageBasePath, testPrjFileName, testLogFileName, testUsersFileName)

  
  val mockedUser = mock[User]
  mockedUser.firstName returns "Anders"
  mockedUser.id returns "111111111111"
  val useCaseContext = mock[UseCaseContext]
  useCaseContext.user returns mockedUser 
  
  val interactor = new GetTotalTimeProjectsInteractor
  interactor.storage = fileStorage
  interactor.log_=(TestUtil.emptyLog) 
  
  val startdate = "2003-01-01"
  val enddate = "2015-02-28"  
  val request = GetTotalTimeProjectsRequestModel(startdate,enddate)
  /*
   * TEST
   */
  "GetTotalTimeProjectsInteractor" should {
    "test" in {
      interactor.execute(request, useCaseContext)
      ok
    }   
  }
  
}