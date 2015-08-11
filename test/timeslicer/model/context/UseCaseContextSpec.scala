package timeslicer.model.context

import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import timeslicer.model.user.UserImpl
import org.specs2.runner.JUnitRunner

/**
 * These are unit tests
 * that we need to run
 */

@RunWith(classOf[JUnitRunner])
class UseCaseContextSpec extends Specification with Mockito {

  "ContextImpl" >> {
    val context = new UseCaseContextImpl()
    val user = mock[UserImpl]
    
    user.name returns "Anders"
    
    "Testing context" in {
      context.user == null must beTrue
      context.user = user;
      context.user != null must beTrue
    }
  }
}