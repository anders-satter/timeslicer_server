package timeslicer.model.usecase.authentication

import timeslicer.model.framework.RequestModel

case class AuthenticationRequestModel(userName:Option[String], email:Option[String]) extends RequestModel {
  override def toString:String = {
    "UserName:" + 
      userName.getOrElse("") + 
      " Email:" + email.getOrElse("")    
  }
}