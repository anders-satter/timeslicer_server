package timeslicer.model.usecase.project

import timeslicer.model.framework.ResponseModel

case class RemoveActivityResponseModel(removalDone:Boolean) extends ResponseModel{
  override def toString:String = {
    String.valueOf(removalDone)
  }
}