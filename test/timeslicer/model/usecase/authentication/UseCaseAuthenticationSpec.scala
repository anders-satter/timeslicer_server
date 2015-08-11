package timeslicer.model.usecase.authentication

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import timeslicer.model.context.UseCaseContextImpl
import org.specs2.mock.Mockito

@RunWith(classOf[JUnitRunner])
class UseCaseAuthenticationSpec extends Specification with Mockito {
  val interactor = new AuthenticationInteractor
  val requestModel = mock[AuthenticationRequestModel]  
  var useCaseContext = null

  "AuthenticationInteractor" should {
    
    "return useCaseContext" in {
      interactor.execute(requestModel, useCaseContext)
        .asInstanceOf[AuthenticationResponseModel]
        .useCaseContext.get must not(beNull)
    }
    
    "return user" in {
    	interactor.execute(requestModel, useCaseContext)
    	.asInstanceOf[AuthenticationResponseModel]
    			.useCaseContext.get.user must not(beNull)
    }
  }
}