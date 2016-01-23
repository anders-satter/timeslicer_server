package timeslicer.model.usecase.user

import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.junit.runner.RunWith
import timeslicer.model.context.UseCaseContext
import timeslicer.model.framework.RequestModel
import timeslicer.model.user.UserImpl
import timeslicer.model.usecase.userid.CreateUserIdInteractor
import timeslicer.model.storage.exception.ItemAlreadyExistsException
import timeslicer.test.util.TestUtil
import timeslicer.model.storage.Storage
import timeslicer.test.util.TestData

@RunWith(classOf[JUnitRunner])
class UseCaseAddUserSpec extends Specification with Mockito{
  /*
   * SETUP
   */
  val interactor = new AddUserInteractor
  
  interactor.log_=(TestUtil.emptyLog) 
  
  val useCaseContext = mock[UseCaseContext]
  val user = new UserImpl
  user.firstName = "Test1"
  user.lastName = "Testson"
  user.id = "000000000000"
  
  val request = AddUserRequestModel(user) 
  val userIdInteractor = new CreateUserIdInteractor
  
  
  
  /*
   * TEST
   */
  "AddUser test" should {
    "fail to add a usr" in {      
        interactor.execute(request, useCaseContext).failure must not beNull
    }
  }
}