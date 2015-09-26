package timeslicer.model.reporting

import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import timeslicer.model.timeslice.TimeSlice



/**
 * These are unit tests
 * that we need to run
 */

@RunWith(classOf[JUnitRunner])
class ReportingUtilSpec extends Specification with Mockito{
  
  /*
   * SETUP
   */
    val testdata = Seq(
    TimeSlice("2015-01-01", "10:00", "2015-01-01", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-02", "10:00", "2015-01-02", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-03", "10:00", "2015-01-03", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-04", "10:00", "2015-01-04", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-05", "10:00", "2015-01-05", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-06", "10:00", "2015-01-06", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-07", "10:00", "2015-01-07", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-08", "10:00", "2015-01-08", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-09", "10:00", "2015-01-09", "10:30", "Prj1", "Act1", Option("")),

    TimeSlice("2015-01-01", "10:00", "2015-01-01", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-02", "10:00", "2015-01-02", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-03", "10:00", "2015-01-03", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-04", "10:00", "2015-01-04", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-05", "10:00", "2015-01-05", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-06", "10:00", "2015-01-06", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-07", "10:00", "2015-01-07", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-08", "10:00", "2015-01-08", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-09", "10:00", "2015-01-09", "11:00", "Prj1", "Act2", Option("")),

    TimeSlice("2015-01-01", "10:00", "2015-01-01", "11:30", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-02", "10:00", "2015-01-02", "11:30", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-03", "10:00", "2015-01-03", "11:30", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-04", "10:00", "2015-01-04", "11:30", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-05", "10:00", "2015-01-05", "11:30", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-06", "10:00", "2015-01-06", "11:30", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-07", "10:00", "2015-01-07", "11:30", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-08", "10:00", "2015-01-08", "11:30", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-09", "10:00", "2015-01-09", "11:30", "Prj2", "Act1", Option("")),

    TimeSlice("2015-01-01", "10:00", "2015-01-01", "12:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-02", "10:00", "2015-01-02", "12:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-03", "10:00", "2015-01-03", "12:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-04", "10:00", "2015-01-04", "12:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-05", "10:00", "2015-01-05", "12:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-06", "10:00", "2015-01-06", "12:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-07", "10:00", "2015-01-07", "12:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-08", "10:00", "2015-01-08", "12:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-09", "10:00", "2015-01-09", "12:00", "Prj2", "Act2", Option("")))
    
  
  /*
   * TEST
   */
  
  "ReportingUtil" should {
    "return 0" in {
      //ReportingUtil.getDuration(projectName, activityName, prjList)
      
      val struct = ReportingUtil.projectActivityStructure(testdata, true);
      val res1 = ReportingUtil.getDuration("Prj1", "Act1", struct)
      val res2 = ReportingUtil.getDuration("Prj1", "Act2", struct)
    	val res3 = ReportingUtil.getDuration("Prj2", "Act1", struct)
    	val res4 = ReportingUtil.getDuration("Prj2", "Act2", struct)
    	println(res1)
    	println(res2)
    	println(res3)
    	println(res4)
      
      ok
    }
  }
}