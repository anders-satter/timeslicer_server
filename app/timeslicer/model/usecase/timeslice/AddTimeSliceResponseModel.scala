package timeslicer.model.usecase.timeslice

import timeslicer.model.framework.ResponseModel

case class AddTimeSliceResponseModel(val isSuccess:Boolean) extends ResponseModel{
  override def toString:String = {
    isSuccess.toString
  }
}