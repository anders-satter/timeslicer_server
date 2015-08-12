package timeslicer.model.timeslice

import timeslicer.model.util.DateTime.DayStringToValueConverter
import timeslicer.model.util.DateTime.ElapsedTimeCalculator


/**
 * TimeSlice is a time period specifying
 * start, end, project, activity, optional comment
 * and a duration method.
 */
case class TimeSlice(start: String,
                     end: String,
                     project: String,
                     activity: String,
                     comment: Option[String] = None) {
  def duration(func:ElapsedTimeCalculator): Long = {
    return func(start, end)
  }
  def dayValue(func:DayStringToValueConverter):Long = {
    return func(start)
  }
}
