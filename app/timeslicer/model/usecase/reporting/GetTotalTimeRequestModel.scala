package timeslicer.model.usecase.reporting

import timeslicer.model.framework.RequestModel

case class GetTotalTimeRequestModel(val startday:String, val endday:String) extends RequestModel {
  override def toString:String = "startday:" + startday + " endday:" + endday
}
