package timeslicer.model.usecase.userid

import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.junit.runner.RunWith
import timeslicer.model.context.UseCaseContext
import timeslicer.model.user.User

@RunWith(classOf[JUnitRunner])
class UserIdInteractorSpec extends Specification with Mockito {
  /*
   * SETUP
   */
  val useCaseContext = mock[UseCaseContext]
  val mockedUser = mock[User]
  mockedUser.id returns "222222222222"
  useCaseContext.user returns mockedUser
  val interactor = new UserIdInteractor
  val requestModel = mock[UserIdRequestModel]
  
  /*
   * TEST
   */
  
  "Userid test" should {
    "create a userid" in {
      interactor.execute(requestModel, useCaseContext).asInstanceOf[UserIdResponseModel].userId != null must beTrue      
    }
  }

}