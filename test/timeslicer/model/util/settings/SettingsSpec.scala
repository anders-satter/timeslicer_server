package timeslicer.model.util.settings
import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
@RunWith(classOf[JUnitRunner])
class SettingsSpec extends Specification{
  "Settings test" should {
    "return active user inactivity timeout delay" in {
      println(Settings.activeUser_inactivityTimeoutDelay)
      println(Settings.usersFileName)
      pending 
    }
  }
}