package timeslicer.model.framework.impl

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito
import timeslicer.model.framework.InteractionIdManager
import timeslicer.model.session.SessionStorage

@RunWith(classOf[JUnitRunner])
class InteractionIdManagerSpec extends Specification with Mockito {
  /*
   * SETUP
   */
  
  val sessionStorage = mock[SessionStorage]
  
  /*
   * TEST
   */  
  
	"InteractionIdManager" should {
	  
		"generate id's" in {
		  
		  val interactionIdManager = InteractionIdManager()
		  interactionIdManager.interactionId("aaaa") == "1" must beTrue		  
		  interactionIdManager.interactionId("aaaa") == "2" must beTrue		  
		  interactionIdManager.interactionId("aaaa") == "3" must beTrue		  
		  interactionIdManager.interactionId("aaaa") == "4" must beTrue
		  
		}
	}
}