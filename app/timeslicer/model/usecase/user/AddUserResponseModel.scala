package timeslicer.model.usecase.user

import timeslicer.model.framework.ResponseModel
import timeslicer.model.user.User

/**
 * Will hold Option(new user) or None depending on if 
 * the new user could be added 
 */
case class AddUserResponseModel(user:Option[User]) extends ResponseModel {
  override def toString:String = {
    user.map(u => u.id).getOrElse("None")
  }
}