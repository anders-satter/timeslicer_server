package timeslicer.model.usecase.user

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito
import timeslicer.test.util.TestUtil
import timeslicer.model.context.UseCaseContext
import play.api.test.FakeApplication

@RunWith(classOf[JUnitRunner])
class UseCaseCreateUserAccountSpec extends Specification with Mockito {
  
  /*this is used only to activate logging in test*/
  FakeApplication(additionalConfiguration = Map(
    "logger.application" -> "INFO"
  ))
  /*
   * SETUP
   */
  val interactor = new CreateUserAccountInteractor
  //interactor.log_=(TestUtil.emptyLog)
  val useCaseContext = mock[UseCaseContext]
  
  /*
   * TEST
   */
  "CreateUserAccount" should {
    "check user login" in {
      interactor
        .execute(CreateUserAccountRequestModel("TestNick",
          "Test2",
          "Testsson",
          "PAssword01",
          Some("abc@defg.se")),
          useCaseContext)
      pending
    }
    "check user login 2" in {
      interactor
        .execute(CreateUserAccountRequestModel("TestNick",
          "Test2",
          "Testsson",
          "PAssword01",
          Some("abc@defg.se")),
          useCaseContext)
      pending
    }
    
  }
}