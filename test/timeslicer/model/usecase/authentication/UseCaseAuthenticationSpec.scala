package timeslicer.model.usecase.authentication

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import timeslicer.model.context.UseCaseContextImpl
import org.specs2.mock.Mockito
import timeslicer.model.user.User
import timeslicer.model.util.Util.EmptyUseCaseContext
import timeslicer.test.util.TestUtil
import timeslicer.model.framework.InteractionLogStringBuilder
import timeslicer.model.storage.Storage
import timeslicer.model.user.PasswordHashCaclulator
import timeslicer.model.user.UserImpl

@RunWith(classOf[JUnitRunner])
class UseCaseAuthenticationSpec extends Specification with Mockito {
  
  val interactor = new AuthenticationInteractor
  
  val mockedUser = new UserImpl
  mockedUser.userName = "TestUser1"
  mockedUser.email = "abc@mymail.com"  
  val passwordSalt = PasswordHashCaclulator.createSalt
  mockedUser.passwordSalt = passwordSalt 
  mockedUser.passwordHash = PasswordHashCaclulator.createHash("password", mockedUser.passwordSalt) 
  
  val mockedStorage = mock[Storage]
  mockedStorage.users returns Some(Seq(mockedUser))

  interactor.log = (TestUtil.emptyLog)
  interactor.storage = mockedStorage

  val requestModel = AuthenticationRequestModel(Some("TestUser1"), None, "password")

  "AuthenticationInteractor" should {

    "return an authenticated user" in {
      var user: User = null
      interactor.execute(requestModel,EmptyUseCaseContext()).success.map(x => {
        user = x.user
      })
      user.isAuthenticated must beTrue
    }
    "return no error" in {
      var error: scala.util.Try[Throwable] = null
      interactor.execute(requestModel, EmptyUseCaseContext()).error.map(errorContainer => errorContainer) must beNone
    }
  }
}