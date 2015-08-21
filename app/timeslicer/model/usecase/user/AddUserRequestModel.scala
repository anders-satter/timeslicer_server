package timeslicer.model.usecase.user

import timeslicer.model.api.RequestModel
import timeslicer.model.user.User

case class AddUserRequestModel(user:User) extends RequestModel {}