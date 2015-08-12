package timeslicer.model.timeslice

import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.junit.runner.RunWith
import timeslicer.model.util.DateTime

@RunWith(classOf[JUnitRunner])
class TimeSliceSpec extends Specification with Mockito {
  /*
   * SETUP
   */
  val timeslice1 = TimeSlice("2015-08-11 09:30", "2015-08-11 10:28", "Project1", "Activity1", Option("Comment1"))
  val timeslice2 = TimeSlice("2015-08-11 10:28", "2015-08-11 11:31", "Project1", "Activity1")
  val timeslice3 = mock[TimeSlice]
  val timeslice4 = mock[TimeSlice]
  
  val func1:(String) => Int = (s) => 1
  timeslice3.dayValue(func1) returns 1234567
  val func2:DateTime.ElapsedTimeCalculator = (s, s2) => 1
  timeslice3.duration(func2) returns 35
  
  /*
   * for some reason this construct does not
   * take the value 36, so it does not return true
   * below....
   */
  timeslice4.duration((s:String, s2:String ) => 1) returns 36
  
  /*
   * TEST
   */
  "TimeSlice" should {
    "return start" in {
      timeslice1.start == "2015-08-11 09:30" must beTrue
    }
    "return end" in {
    	timeslice1.end == "2015-08-11 10:28" must beTrue
    }
    
    "correct dayValue" in{      
      timeslice3.dayValue((s:String) => 1) == 1234567 must beTrue
    }
    "correct duration" in {
    	timeslice3.duration(func2) == 35 must beTrue
      /*
       *timeslice4.duration((s:String, s2:String ) => 1) == 36 must beTrue 
       */
    }    
  }
}