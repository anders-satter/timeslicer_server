package timeslicer.models.util
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
class TestFileUtilSpec extends Specification {
  "FileUtile.test" >> {
    "should return 1" >> {
      FileUtil.test == 1
    }
    "should not return 0" >> {
      FileUtil.test != 0
    }
  }
  "FileUtile.toSource" >> {
    "should convert?" >> {
      val str: String = "hsieh\nsolo\nlikewise"
      val stream: InputStream = new ByteArrayInputStream(str.getBytes)
      val encoding = "UTF8"
      //println(FileUtil.toSource(stream, encoding))
      FileUtil.toSource(stream, encoding) must contain("hsieh") 
    }
  }
}