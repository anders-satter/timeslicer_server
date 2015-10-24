package timeslicer.model.session

import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import timeslicer.model.user.User

@RunWith(classOf[JUnitRunner])
class SessionSpec extends Specification with Mockito {

  /*
  * SETUP
  */
  val mockedUser = mock[User]
  mockedUser.userName returns "anders"
  mockedUser.id returns "111111111111"
  
  val mockedSession = mock[Session]

  /*
   * TEST
   */
  "SessionSpec" should {
    "create a new session" in {
      mockedSession != null
    }
    "set user in" in {
      mockedSession.user returns mockedUser
    	mockedSession.user != null
    }
  }
  
}