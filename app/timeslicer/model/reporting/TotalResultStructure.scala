package timeslicer.model.reporting;
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

case class ProjectActivityDayResult(val day: String, val project: String, val activity: String, val duration: Long)

case class DailyResultStructure(val startday: String, val endday: String, val dayMap: Array[Map[String, Seq[ProjectActivityDayResult]]]) {
  private val LINE_FEED = '\n'
  override def toString() = {
    val buff = new StringBuilder
    buff.append("start:" + startday + " end:" + endday)
    dayMap.foreach(item=> {
      item.foreach(day => {
        buff.append(day._1)
        buff.append(LINE_FEED + "->")
        day._2.foreach(a => {          
        	buff.append(a.project + '|'+ a.activity + '|'+ String.valueOf(a.duration) + LINE_FEED)
        })        
      })
    })
    buff.toString
  }

}

/**
 * A result structure that holds all projects within the reporting interval
 */
case class TotalResultStructure(val startday: String, val endday: String, val projects: Seq[SumProject]) {
  def totalDuration: Long = projects.toList.foldLeft(0L)((acc, project) => acc + project.duration)
}

case class SumProject(val name: String, val activities: Seq[SumActivity]) {
  def duration = activities.toList.foldLeft(0L)((x, activity) => x + activity.duration)
  def percentOfTotalHours(totResultDuration: Long): Double = this.duration / totResultDuration
}

class EmptySumProject(override val name: String = "Empty", override val activities: Seq[EmptySumActivity] = Seq(new EmptySumActivity("", Seq()))) extends SumProject(name, activities) {
  override def percentOfTotalHours(totResultDuration: Long): Double = u.round2(0)
}

case class SumActivity(val name: String, val timeslices: Seq[TimeSlice]) {
  /**
   * will calculate the duration of all timeslices that make up
   * this activity and thus shows the tot duration for that activity
   */
  def duration: Long =
    timeslices.toList.foldLeft(0L)((x: Long, timeSlice) => timeSlice.duration(dt.elapsedMinutes) + x)
  def percentOfProject(totProjectDuration: Long): Double = u.round2(this.duration / totProjectDuration)
  def percentOfTotalHours(totResultDuration: Long): Double = u.round2(this.duration / totResultDuration)
}

class EmptySumActivity(override val name: String = "Empty", override val timeslices: Seq[TimeSlice] = Seq()) extends SumActivity(name, timeslices) {
  override def duration: Long = 0
  override def percentOfProject(totProjectDuration: Long): Double = u.round2(0)
  override def percentOfTotalHours(totResultDuration: Long): Double = u.round2(0)
}



