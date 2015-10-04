package timeslicer.model.usecase.timeslice

import timeslicer.model.framework.RequestModel
import timeslicer.model.timeslice.TimeSlice

case class AddTimeSliceRequestModel(val timeslice:TimeSlice) extends RequestModel{
  override def toString:String = {
    timeslice.toString
  }
}