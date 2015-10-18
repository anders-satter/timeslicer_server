package timeslicer.model.user

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import timeslicer.model.util.settings.Settings
import scala.concurrent.duration.FiniteDuration

@RunWith(classOf[JUnitRunner])
class UserManagerImplSpec extends Specification{
  "UserManagerImpl test" should {
    "load an ActiveUserStorage" in {
      UserManagerImpl.activeUserStorage != null
    }
    "assert that ActiveUserStorage inactiveTimeoutDelay equals value in settings.properties" in {
      /*
       * get the inactivityTimeoutDelay from Settnings.properties
       */
      val inactivityDelayInSettings:String = Settings.activeUser_inactivityTimeoutDelay    
      /*
       * convert it to a Long      
       */
      val settingsValue:Long = java.lang.Long.parseLong(inactivityDelayInSettings)
      /*compare it*/
    	settingsValue == UserManagerImpl.activeUserStorage.properties.inactivityTimeoutDelay
      
    }
  }

}