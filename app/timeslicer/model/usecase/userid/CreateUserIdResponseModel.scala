package timeslicer.model.usecase.userid

import timeslicer.model.framework.ResponseModel

case class CreateUserIdResponseModel(val userId:String) extends ResponseModel {
  override def toString = {
    userId
  }
  
}