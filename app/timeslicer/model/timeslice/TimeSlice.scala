package timeslicer.model.timeslice

import timeslicer.model.util.DateTime.DayStringToValueConverter
import timeslicer.model.util.DateTime.ElapsedTimeCalculator
import timeslicer.exception.TimeslicerException

/**
 * TimeSlice is a time period specifying
 * start, end, project, activity, optional comment
 * and a duration method.
 */
case class TimeSlice(val startdate: String, 
                     val starttime: String,
                     val enddate: String, 
                     val endtime: String,
                     val project: String,
                     val activity: String,
                     val comment: Option[String] = None) {
  /**
   * Calculates the duration of the TimeSlice and
   * converts in ms
   */
  def duration(func: ElapsedTimeCalculator): Long = {
    val start = startdate + " " + starttime
    val end = enddate + " " + endtime
    return func(start, end)
  }

  /**
   * Returns the day value in ms
   */
  def dayValue(func: DayStringToValueConverter): Long = {
    if (startdate != enddate) {
      throw new TimeslicerException("startdate and enddate should be the same")
    }
    val start = startdate + " " + starttime
    return func(start)
  }
  
  override def toString:String = {
    val SEMICOLON = ";"
    val str:StringBuilder = new StringBuilder
    str.append(startdate)
    str.append(SEMICOLON)
    str.append(starttime)
    str.append(SEMICOLON)
    str.append(enddate)
    str.append(SEMICOLON)
    str.append(endtime)
    str.append(SEMICOLON)
    str.append(project)
    str.append(SEMICOLON)
    str.append(activity)
    str.append(SEMICOLON)
    comment.foreach(c => str.append(c))    
    str.append(SEMICOLON)
    
    str.toString
  }
}