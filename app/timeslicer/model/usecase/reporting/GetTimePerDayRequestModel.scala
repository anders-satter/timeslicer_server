package timeslicer.model.usecase.reporting

import timeslicer.model.framework.RequestModel

case class GetTimePerDayRequestModel(val startday:String, val endday:String) extends RequestModel {
  override def toString:String = "startday:" + startday + " endday:" + endday
}