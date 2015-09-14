package timeslicer.model.usecase.timeslice

import timeslicer.model.framework.RequestModel

case class GetTimeSlicesRequestModel(val startdate:String, val enddate:String) extends RequestModel{
  override def toString = {
     "start:"+ startdate +" end:" + enddate  
  }
}