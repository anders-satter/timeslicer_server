package timeslicer.model.usecase.user

import timeslicer.model.user.User
import timeslicer.model.framework.ResponseModel

case class GetUsersResponseModel(userList:Seq[User]) extends ResponseModel {
  override def toString:String = {
    val message = " users returned";
    if(userList.nonEmpty){
        String.valueOf(userList.length) + message 
    } else {
      "0" + message
    }
  }
}