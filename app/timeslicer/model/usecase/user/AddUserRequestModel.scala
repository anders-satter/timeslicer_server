package timeslicer.model.usecase.user

import timeslicer.model.framework.RequestModel
import timeslicer.model.user.User

case class AddUserRequestModel(user:User) extends RequestModel {}