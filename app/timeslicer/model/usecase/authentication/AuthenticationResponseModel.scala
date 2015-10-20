package timeslicer.model.usecase.authentication

import timeslicer.model.framework.ResponseModel
import timeslicer.model.context.UseCaseContext


case class AuthenticationResponseModel(useCaseContext:UseCaseContext) extends ResponseModel{
  override def toString = {
    useCaseContext.user.id
  }
}