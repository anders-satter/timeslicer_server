package timeslicer.model.usecase.user

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito
import timeslicer.model.storage.Storage
import timeslicer.model.usecase.userid.CreateUserIdRequestModel
import timeslicer.model.user.User
import timeslicer.test.util.TestUtil
import timeslicer.test.util.TestData
import timeslicer.model.usecase.userid.CreateUserIdInteractor
import timeslicer.model.util.Util.EmptyUseCaseContext

@RunWith(classOf[JUnitRunner])
class UserIdInteractorSpec extends Specification with Mockito {
  /*
   * SETUP
   */
    val user = mock[User]
  user.firstName returns "Test1"
  user.lastName returns "Testson"
  user.id returns "000000000000"

  val request = CreateUserIdRequestModel()
  val interactor = new CreateUserIdInteractor
  interactor.log_=(TestUtil.emptyLog)

  val mockerStorage = mock[Storage]
  mockerStorage.users() returns TestData.testUsers
  val emptyStorage = mock[Storage]
  emptyStorage.users() returns None
  
  interactor.storage = mockerStorage

  /*
   * TEST
   */  
  
	"UserIdInteractor" should {
	  
		"create a userid with the required length" in {
		  val result = interactor.execute(request, EmptyUseCaseContext())
		  result.success.get.userId.length == 12
		}
	}
}