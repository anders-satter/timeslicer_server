package timeslicer.model.usecase.authentication

import timeslicer.model.api.ResponseModel
import timeslicer.model.context.UseCaseContext


case class AuthenticationResponseModel(useCaseContext:Option[UseCaseContext]) extends ResponseModel{} 