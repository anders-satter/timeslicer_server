package timeslicer.model.usecase.reporting

import timeslicer.model.framework.ResponseModel
import timeslicer.model.reporting.TotalResultStructure

case class GetTotalTimeResponseModel(resultStructure:TotalResultStructure) extends ResponseModel{
  override def toString:String = {
    "No of projects found: " + resultStructure.projects.length 
  }
}