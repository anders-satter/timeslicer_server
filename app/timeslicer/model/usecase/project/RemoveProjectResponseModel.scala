package timeslicer.model.usecase.project

import timeslicer.model.framework.ResponseModel

case class RemoveProjectResponseModel(couldRemove:Boolean) extends ResponseModel {
  override def toString:String = {
    String.valueOf(couldRemove)
  }
}