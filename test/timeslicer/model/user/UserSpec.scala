package timeslicer.model.user
import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import play.api.test.WithBrowser
import java.io.InputStream
import java.io.ByteArrayInputStream

/**
 * These are unit tests
 * that we need to run
 */

@RunWith(classOf[JUnitRunner])
class UserSpec extends Specification {

  "UserImpl" >> {
    val argumentGenerator: Int => String = (length: Int) => {
      val builder = new StringBuilder()
      (0 to length - 1).foreach(i => builder.append("x"))
      builder.toString
    }
    val user = new UserImpl()
    
    
    "Test firstName field" in {
      { user.firstName = argumentGenerator(0) } must throwA[IllegalArgumentException];
      { user.firstName = argumentGenerator(21) } must throwA[IllegalArgumentException];
      { user.firstName = argumentGenerator(1) } must not(throwA[IllegalArgumentException]);
      { user.firstName = argumentGenerator(20) } must not(throwA[IllegalArgumentException]);
      { user.firstName = null } must throwA[IllegalArgumentException];
    }

    "Test lastName field" in {
    	{ user.lastName = argumentGenerator(0) } must throwA[IllegalArgumentException];
    	{ user.lastName = argumentGenerator(31) } must throwA[IllegalArgumentException];
    	{ user.lastName = argumentGenerator(6) } must not(throwA[IllegalArgumentException]);
    	{ user.lastName = argumentGenerator(30) } must not(throwA[IllegalArgumentException]);
    	{ user.lastName = null } must throwA[IllegalArgumentException];
    }

    "Test id field" in {
      { user.id = argumentGenerator(10) } must throwA[IllegalArgumentException];
      { user.id = argumentGenerator(13) } must throwA[IllegalArgumentException];
      { user.id = argumentGenerator(12) } must not(throwA[IllegalArgumentException]);
      { user.id = null } must throwA[IllegalArgumentException];
      
    }

    "Test authenticated field" in {
      //assignment
      user.isAuthenticated = true
      //should equal true
      user.isAuthenticated == true
    }
    "Test authorized field" in {
      //assignment
      user.isAuthorized = true
      //should equal true
      user.isAuthorized == true
    }
    "Test email field" in {
      user.email == None must beTrue;
      { user.email = "a" } must (throwA[IllegalArgumentException]);
      { user.email = "a@" } must (throwA[IllegalArgumentException]);
      { user.email = "a@." } must not((throwA[IllegalArgumentException]));
      { user.email = "a@s.se" } must not((throwA[IllegalArgumentException]));           
      user.email.get == "a@s.se" must beTrue;      
    }
  }
}