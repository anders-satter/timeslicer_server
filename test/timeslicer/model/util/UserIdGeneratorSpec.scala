package timeslicer.model.util

import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class UserIdGeneratorSpec extends Specification with Mockito{
  /*
   * SETUP
   */
     
  /*
   * TEST
   */
  "UserIdGenerator test" should {
    "generate a userId with length of 12" in {
      val userid = StringIdGenerator.userId()
      println(userid)
      userid.length()==12 must beTrue
    }    
  }
  
}