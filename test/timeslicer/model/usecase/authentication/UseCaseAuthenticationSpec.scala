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

@RunWith(classOf[JUnitRunner])
class UseCaseAuthenticationSpec extends Specification with Mockito {
  val interactor = new AuthenticationInteractor
  
  interactor.log_=(TestUtil.emptyLog) 
  
  val requestModel = mock[AuthenticationRequestModel]
  var noneCaseContext = new EmptyUseCaseContext

  "AuthenticationInteractor" should {

    "return useCaseContext with user" in {
      var user: User = null
      interactor.execute(requestModel).success.map(x => {
        user = x.useCaseContext.user
      })
      interactor.execute(requestModel).success.map(x => {
        x.useCaseContext.user must not(beNull)
      })
      
      val user1 = for {
//        s <- interactor.execute(requestModel).success.map(x => x.useCaseContext.user)
        s <- interactor.execute(requestModel).success.map(x => x).map(x => x.useCaseContext.user)
      } yield s
      //println(user1)
      val user2 = interactor.execute(requestModel).success.map(x => x).map(x => x.useCaseContext.user).get
      //println(user2)

      //work with the user
      interactor.execute(requestModel).success.map(rm => rm).map {
        x =>
          {
            val user = x.useCaseContext.user
            //println("Here we will print the users name:")
            //println(user.firstName + " " + user.lastName)
          }
      }
      user must not(beNull)
    }
    "return no error" in {
      var error: scala.util.Try[Throwable] = null
      interactor.execute(requestModel).error.map(errorContainer => errorContainer) must beNone
    }
  }
}