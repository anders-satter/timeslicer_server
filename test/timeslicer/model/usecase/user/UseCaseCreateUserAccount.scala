package timeslicer.model.usecase.user

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito
import timeslicer.test.util.TestUtil
import timeslicer.model.context.UseCaseContext

@RunWith(classOf[JUnitRunner])
class UseCaseCreateUserAccount extends Specification with Mockito {
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

    
    
    "check user login in" in {
      interactor
        .execute(CreateUserAccountRequestModel("TestNick",
          "Test",
          "Testsson",
          "PAssword01",
          Some("abc@defg.se")),
          useCaseContext)
      pending
    }
    "check user login 2 in" in {
      interactor
        .execute(CreateUserAccountRequestModel("TestNick",
          "",
          "Testsson",
          "PAssword01",
          Some("abc@defg.se")),
          useCaseContext)
      pending
    }
  }
}