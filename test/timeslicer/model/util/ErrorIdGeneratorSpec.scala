package timeslicer.model.util

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito

@RunWith(classOf[JUnitRunner])
class ErrorIdGeneratorSpec extends Specification with Mockito {
  /*
   * SETUP
   * Nothing needs to be set up here
   */
  
  /*
   * TEST
   */  
  
	"ErrorIdGenerator" should {
		"generate id's" in {
		 val errorId = StringIdGenerator.errorId
		 //some questions are going to be answered
		 errorId.length() > 0 
		}
	}
}