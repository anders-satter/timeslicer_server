package timeslicer.model.usecase.reporting

import timeslicer.model.framework.ResponseModel
import timeslicer.model.reporting.TotalResultStructure

case class GetTotalTimeProjectsResponseModel(resultStructure:TotalResultStructure) extends ResponseModel{
  //override def toString:String = String.valueOf(resultStructure.totaltime)
}