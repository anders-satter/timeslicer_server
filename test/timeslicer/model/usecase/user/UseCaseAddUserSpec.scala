package timeslicer.model.usecase.user

import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.junit.runner.RunWith
import timeslicer.model.context.UseCaseContext
import timeslicer.model.api.RequestModel
import timeslicer.model.user.UserImpl
import timeslicer.model.usecase.userid.CreateUserIdInteractor

@RunWith(classOf[JUnitRunner])
class UseCaseAddUserSpec extends Specification with Mockito{
  /*
   * SETUP
   */
  val interactor = new AddUserInteractor
  val useCaseContext = mock[UseCaseContext]
  val user = new UserImpl
  user.firstName = "Test1"
  user.lastName = "Testson"
  val request = AddUserRequestModel(user) 
  val userIdInteractor = new CreateUserIdInteractor
  
  /*
   * TEST
   */
  "AddUser test" should {
    "add a new user" in {
      val response = interactor
        .execute(request, useCaseContext)        
      ok
    }
  }
  
}