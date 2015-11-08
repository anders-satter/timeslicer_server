package timeslicer.model.util

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import play.api.test.WithBrowser
import java.io.InputStream
import java.io.ByteArrayInputStream
import java.util.Base64
import timeslicer.model.util.{DateTime => dt}

/**
 * These are unit tests
 * that we need to run
 */

@RunWith(classOf[JUnitRunner])
class DateTimeSpec extends Specification {
  "DayIncrementor" >> {
    "should make a list of dates" >> {
      
      //val dayIncrementor = DateTime.dayIncrementor(DateTime.getDayList(startDay, endDay))
      /*
       * the a day in ms format
       */
      //val dayInMs:Long = DateTime.day("2015-01-01").toMs
      //println(DateTime.day("2015-01-01"))
      //val d = DateTime.day("2015-01-01")
      val d:Long = DateTime.dayMs("2015-01-01")
      //println(d)
      1==1
    }
  }  
  
  def b64EncodeStr(str: String) = Base64.getEncoder().encodeToString(str.getBytes)
  
  "should test java encoding" in {
    println("Hello")
	  val dayb64 = b64EncodeStr(dt.Now(dt.now, dt.getDayValueInStr, dt.fullTimePart).day)
	  println(dt.Now(dt.now, dt.getDayValueInStr, dt.fullTimePart).dayAndTime)
    pending
  }
}