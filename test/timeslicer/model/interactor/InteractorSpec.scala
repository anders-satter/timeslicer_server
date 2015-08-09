package timeslicer.model.interactor
import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import play.api.test.WithBrowser
import java.io.InputStream
import java.io.ByteArrayInputStream
import timeslicer.model.api.RequestModel
import timeslicer.model.api.ResponseModel

/**
 * These are unit tests
 * that we need to run
 */

@RunWith(classOf[JUnitRunner])
class InteractorSpec extends Specification {
  "Interactor test" >> {
    "should implement interactor trait" >> {
      var requestValue = ""
      var responseValue = ""
      class InteractorImpl extends Interactor {
        override def execute(req: RequestModel, resp: ResponseModel) = {
          requestValue = req.userToken.get
          responseValue = resp.responseValue1.get
        }
      }

      val interactorImpl = new InteractorImpl
      //create a RequestModel implementation
      val requestModel = new RequestModel {
        override def userToken: Option[String] = {
          return Option("token")
        }
      }
      //create a ResponseModel implementation
      val responseModel = new ResponseModel {
        override def responseValue1: Option[String] = {
          return Option("value1")
        }
      }
      
      interactorImpl.execute(requestModel, responseModel)
      requestValue == "userToken"
      requestValue == "userToken"
      responseValue == "value1"
      responseValue != "value2"       
    }
  }
}