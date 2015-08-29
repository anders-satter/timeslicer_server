package timeslicer.model.usecase.user

import timeslicer.model.api.ResponseModel
import timeslicer.model.user.User

/**
 * Will hold Option(new user) or None depending on if 
 * the new user could be added 
 */
case class AddUserResponseModel(user:Option[User]) extends ResponseModel {}