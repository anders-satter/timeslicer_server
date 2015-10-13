package timeslicer.model.user.activeuser

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import timeslicer.test.util.TestUtil
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ActiveUserMapSpec extends Specification{
  /*
   * SETUP
   */
  
  val user = TestUtil.testUser
  var activeUser:ActiveUser = null
  var latestTouch = 0L
  
  "ActiveUserTest" should{
    "create an activeuser should increase latestTouch from 0L" in {
      activeUser = ActiveUser(user)
      latestTouch = activeUser.latestTouch 
      latestTouch > 0L                  
    }
    "retrieve activeUser.user should increase latestTouch" in {
      activeUser.user.userName
      val currentTouchTime = latestTouch
      latestTouch = activeUser.latestTouch 
      activeUser.latestTouch > currentTouchTime
    }
    "retrieve user with userWithoutTouch should not modify latestTouch" in {
    	activeUser.userWithoutTouch.userName
      activeUser.latestTouch == latestTouch
    }
    
    "retrieve user with userWithoutTouch should not modify latestTouch" in {
    	activeUser.userWithoutTouch.userName
    	activeUser.latestTouch == latestTouch
    }
  }
}