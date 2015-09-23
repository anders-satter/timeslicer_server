package timeslicer.model.reporting
import timeslicer.model.util.{ DateTime => dt, Util => u }
import timeslicer.model.timeslice.TimeSlice

/**
 * Should be presented as a matrix showing this:
 * 
 *  Prj  Act  date1 date2 date3 date4 ... Sum
 *  ---  ---  ----- ----- ----- ----- --- ---
 *  Prj1 Act1   1     1     1     1         4
 *  Prj1 Act2   0     1     3     3         7
 *  Prj2 Act1   0     7     9     3         19
 *  ---  ---  ----- ----- ----- ----- ---  --- 
 *  Sum         2     9     10    7         29
 */

case class DailyResultStructure(val startday:String, val endday:String, val projects:Seq[SumProject]) {
  def compile = ???
}

/**
 * A result structure that holds all projects within the reporting interval
 */
case class TotalResultStructure(val startday: String, val endday: String, val projects: Seq[SumProject]) {
  def totalDuration: Long = projects.toList.foldLeft(0L)((acc, project) => acc + project.duration)
}

case class SumProject(val name: String, val activities: Seq[SumActivity]){
  def duration = activities.toList.foldLeft(0L)((x, activity) => x + activity.duration)
	def percentOfTotalHours(totResultDuration:Long):Double = this.duration/totResultDuration
}

case class SumActivity(val name: String, val timeslices: Seq[TimeSlice]) {
  /**
   * will calculate the duration of all timeslices that make up
   * this activity and thus shows the tot duration for that activity
   */
  def duration: Long =
    timeslices.toList.foldLeft(0L)((x: Long, timeSlice) => timeSlice.duration(dt.elapsedMinutes) + x)
  def percentOfProject(totProjectDuration:Long):Double = u.round2(this.duration/totProjectDuration)
  def percentOfTotalHours(totResultDuration:Long):Double = u.round2(this.duration/totResultDuration)
}
