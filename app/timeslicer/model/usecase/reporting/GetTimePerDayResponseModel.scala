package timeslicer.model.usecase.reporting

import timeslicer.model.framework.ResponseModel
import timeslicer.model.reporting.DailyResultStructure

case class GetTimePerDayResponseModel(val resultStructure: DailyResultStructure) extends ResponseModel {
  override def toString:String = String.valueOf(resultStructure.dayMap.length)
}