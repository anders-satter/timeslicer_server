package timeslicer.model.usecase.authentication

import timeslicer.model.interactor.Interactor
import timeslicer.model.api.RequestModel
import timeslicer.model.api.ResponseModel
import timeslicer.model.context.UseCaseContext
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.user.UserImpl

/**
 * The use case should return a ResponseModel with a 
 * UseCaseContext that is filled with a User
 */
class AuthenticationInteractor extends Interactor {
  override def execute(req: RequestModel, context: UseCaseContext): ResponseModel = {
    val useCaseContext = new UseCaseContextImpl();
    val user = new UserImpl
    user.id = "111111111111"
    user.name = "Anders"
    user.isAuthenticated = true
    user.isAuthorized = true
    user.email = "example@email.com"
    useCaseContext.user = new UserImpl
    return AuthenticationResponseModel(Option(useCaseContext))
  }
}