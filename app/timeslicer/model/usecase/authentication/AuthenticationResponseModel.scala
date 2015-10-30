package timeslicer.model.usecase.authentication

import timeslicer.model.framework.ResponseModel
import timeslicer.model.context.UseCaseContext
import timeslicer.model.user.User


case class AuthenticationResponseModel(user:User) extends ResponseModel{
  override def toString = {
    String.valueOf(user.isAuthenticated)
  }
}