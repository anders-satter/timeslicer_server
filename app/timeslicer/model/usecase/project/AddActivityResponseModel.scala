package timeslicer.model.usecase.project

import timeslicer.model.framework.ResponseModel

case class AddActivityResponseModel(additionDone:Boolean) extends ResponseModel {
  override def toString:String = {
    String.valueOf(additionDone)
  }
}