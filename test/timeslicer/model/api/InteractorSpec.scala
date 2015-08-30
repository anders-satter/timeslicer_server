package timeslicer.model.interactor
import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import play.api.test.WithBrowser
import java.io.InputStream
import java.io.ByteArrayInputStream
import timeslicer.model.context.UseCaseContext
import timeslicer.model.context.UseCaseContextImpl
import org.specs2.mock.Mockito
import org.specs2.mutable.Before
import timeslicer.model.api.ResponseModel
import timeslicer.model.api.RequestModel

/**
 * Interactor unit tests
 */

@RunWith(classOf[JUnitRunner])
class InteractorSpec extends Specification with Mockito{
  /*
   * Definitions
   */
  class TestResponseModel extends ResponseModel {
    def responseValue: Option[String] = {
      Option("value1")
    }
  }
  class TestInteractor extends Interactor[RequestModel,TestResponseModel] {
    override def execute(reg: RequestModel, context: UseCaseContext): TestResponseModel = {
      return new TestResponseModel
    }
  }      
  val mockedRequest = mock[RequestModel];
  val mockedUseCaseContext = mock[UseCaseContext];
  val mockedInteractor = new TestInteractor;

  /*
   * Tests
   */
  "Interactor" should {
    "return value1" in {
      mockedInteractor.execute(mockedRequest, mockedUseCaseContext).responseValue.get == "value1" must beTrue;
    }    
    "not return value2" in {
    	mockedInteractor.execute(mockedRequest, mockedUseCaseContext).responseValue.get == "value2" must beFalse;
    }        
  }
}