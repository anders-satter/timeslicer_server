package timeslicer.model.user

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mock.Mockito

@RunWith(classOf[JUnitRunner])
class HashCalculationSpec extends Specification with Mockito {
  /*
   * SETUP
   */
   val specCalculator = PasswordHashCaclulator
   val password = "password"
   val salt = specCalculator.createSalt
  /*
   * TEST
   */

  "HashCalculationTest" should {
    "calculate a hash" in {
      specCalculator.createHash(password, salt).length()>50 must beTrue      
    }
  }
}