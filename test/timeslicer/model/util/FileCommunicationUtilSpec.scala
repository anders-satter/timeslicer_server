package timeslicer.model.util
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
class FileCommunicationUtilSpec extends Specification {
  "FileUtil.test" >> {
    "should return 1" >> {
      FileCommunicationUtil.test == 1
    }
    "should not return 0" >> {
      FileCommunicationUtil.test != 0
    }
  }
  "FileCommunicationUtil.toSource" >> {
    "should test the size of the " >> {
      val str: String = "hsie\nhsololike\nwise"
      val stream: InputStream = new ByteArrayInputStream(str.getBytes)
      val encoding = "UTF-8"

      FileCommunicationUtil.toSource(stream, encoding).size == 19 must beTrue
    }
  }

  "FileCommunicationUtil.readFromFile" >> {
    "should succeed to read file content of a file " >> {
      val fileContent = FileCommunicationUtil.readFromFile("/Users/anders/dev/eclipse_ws1/timeslicer_server/test/data/logOneDay.txt", "UTF-8")
      fileContent(2) must contain("2013-12-13 11:00")
    }
  }
  
  "FileCommunicationUtil reading the settings file" >> {
    "should be able to access to setting.properties file" >> {
      val fileContent = FileCommunicationUtil.readFromFile("settings.properties", "UTF-8")      
      fileContent(2) must contain("#")
    }
    
    "FileCommunicationUtil" should {
      "return lines" in {
        val fileContent = FileCommunicationUtil.readFromFile("test/data/logOneDay.txt", "UTF-8")
        fileContent.map(println)        
        fileContent(2) must contain("2013-12-13 11:00")
        ok
      }
    }
  }
  
  

}