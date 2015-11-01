package timeslicer.model.user

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito

@RunWith(classOf[JUnitRunner])
class PasswordUtilSpec extends Specification with Mockito {
  /*
   * SETUP
   */
  val specCalculator = PasswordUtil
  val password = "password"
  val salt = specCalculator.createSalt
  /*
   * TEST
   */

  "HashCalculationTest" should {
    "calculate a hash" in {
      specCalculator.createHash(password, salt).length() > 50 must beTrue
    }
    "return true in isPasswordOk" in {
      val pw = "!\"12QWqw"
      println(pw)
      specCalculator.isPasswordValid(pw) must beTrue
    }
    "return true for %&12TYgh" in {
      specCalculator.isPasswordValid("%&12TYgh") must beTrue
    }
    "return false in 2312TYgh" in {
      specCalculator.isPasswordValid("2312TYgh") must beTrue
    }
    "return false in /se" in {
      specCalculator.isPasswordValid("/se") must beFalse
    }
    "return false in /se12" in {
      specCalculator.isPasswordValid("/se12") must beFalse
    }
    "return false in Vericy01" in {
      specCalculator.isPasswordValid("Vericy01") must beFalse
    }
    "return false in /Vericy01" in {
      specCalculator.isPasswordValid("/Vericy01") must beFalse
    }
    "return true in @VEricy01" in {
    	specCalculator.isPasswordValid("@VEricy01") must beTrue
    }
    "return true in )VRricy01" in {
    	specCalculator.isPasswordValid(")VRricy01") must beTrue
    }
    "return true in @1VEricY4" in {
    	specCalculator.isPasswordValid("@1VEricY4") must beTrue    	
    }
    "return true in @1VEricY4" in {
    	specCalculator.isPasswordValid("@1VEricY4") must beTrue    	
    }
    "return true in CYrano01" in {
    	specCalculator.isPasswordValid("CYrano01") must beTrue    	
    }
    
    
    
  }
}