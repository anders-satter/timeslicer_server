package timeslicer.model.usecase.user

import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.junit.runner.RunWith
import timeslicer.test.util.TestUtil
import timeslicer.model.context.UseCaseContext
import timeslicer.model.user.User
import timeslicer.model.storage.Storage
import timeslicer.test.util.TestData

@RunWith(classOf[JUnitRunner])
class UseCaseGetUsersSpec extends Specification with Mockito {
  /*
   * SETUP
   */

  val useCaseContext = mock[UseCaseContext]

  val user = mock[User]
  user.firstName returns "Test1"
  user.lastName returns "Testson"
  user.id returns "000000000000"

  val request = GetUsersRequestModel()
  val interactor = new GetUsersInteractor
  interactor.log_=(TestUtil.emptyLog)

  val mockerStorage = mock[Storage]
  mockerStorage.users() returns TestData.testUsers
  val emptyStorage = mock[Storage]
  emptyStorage.users() returns None
  
  interactor.storage = mockerStorage

  /*
   * TEST
   */
  "UserInteractor" should {
    
    "check the test data" in {
      interactor.storage.users().get.length > 0      
    }
    
    "return 4 test users" in {
    	interactor.execute(request, useCaseContext).success.get.userList.length==4
    }

    "return a user list with 0 users" in {
      interactor.storage = emptyStorage
    	interactor.execute(request, useCaseContext).success.get.userList.length==0
    }
  }
}