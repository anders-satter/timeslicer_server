package timeslicer.model.usecase.user

import timeslicer.model.user.User
import timeslicer.model.framework.ResponseModel

case class GetUsersResponseModel(userList:Seq[User]) extends ResponseModel