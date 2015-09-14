package timeslicer.model.usecase.timeslice

import timeslicer.model.framework.ResponseModel
import timeslicer.model.timeslice.TimeSlice

case class GetTimeSlicesResponseModel(timeslices: Seq[TimeSlice]) extends ResponseModel {
  override def toString = {
    "timeslices.length:" + String.valueOf(Option(timeslices).getOrElse(Seq()).length)
  }
}