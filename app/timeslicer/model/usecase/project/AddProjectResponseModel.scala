package timeslicer.model.usecase.project

import timeslicer.model.framework.ResponseModel

case class AddProjectResponseModel(additionDone:Boolean) extends ResponseModel{
  override def toString:String = {
    String.valueOf(additionDone)
  }

}