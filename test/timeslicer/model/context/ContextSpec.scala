package timeslicer.model.context

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import play.api.test.WithBrowser
import java.io.InputStream
import java.io.ByteArrayInputStream
import timeslicer.model.user.UserImpl

/**
 * These are unit tests
 * that we need to run
 */

@RunWith(classOf[JUnitRunner])
class ContextSpec extends Specification {

  "ContextImpl" >> {
    val context: ContextImpl = new ContextImpl()
    val user = new UserImpl();
    user.name = "Anders";

    "Testing context" in {
      context.user == null must beTrue
      context.user = user;
      context.user != null must beTrue
    }
  }
}