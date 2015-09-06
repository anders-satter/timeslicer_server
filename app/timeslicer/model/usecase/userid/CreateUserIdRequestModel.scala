package timeslicer.model.usecase.userid

import timeslicer.model.framework.RequestModel
import timeslicer.model.user.User

class CreateUserIdRequestModel(user:User) extends RequestModel{
  override def toString ={ 
    user.toString
  }
}