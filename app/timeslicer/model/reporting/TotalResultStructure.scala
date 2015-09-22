package timeslicer.model.reporting
import timeslicer.model.util.{ DateTime => dt, Util => u }
import timeslicer.model.timeslice.TimeSlice


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
