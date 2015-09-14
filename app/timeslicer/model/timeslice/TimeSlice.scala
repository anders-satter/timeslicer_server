package timeslicer.model.timeslice

import timeslicer.model.util.DateTime.DayStringToValueConverter
import timeslicer.model.util.DateTime.ElapsedTimeCalculator
import timeslicer.exception.TimeslicerException

/**
 * TimeSlice is a time period specifying
 * start, end, project, activity, optional comment
 * and a duration method.
 */
case class TimeSlice(startdate: String, 
                     starttime: String,
                     enddate: String, 
                     endtime: String,
                     project: String,
                     activity: String,
                     comment: Option[String] = None) {
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
}