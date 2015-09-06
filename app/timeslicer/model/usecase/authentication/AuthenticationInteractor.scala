package timeslicer.model.usecase.authentication

import timeslicer.model.framework.RequestModel
import timeslicer.model.framework.ResponseModel
import timeslicer.model.context.UseCaseContext
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.user.UserImpl
import timeslicer.model.api.XInteractor
import timeslicer.model.api.ResultOld


/**
 * The use case should return a ResponseModel with a 
 * UseCaseContext that is filled with a User
 */

class AuthenticationInteractor extends XInteractor[AuthenticationRequestModel,ResultOld[AuthenticationResponseModel] ] {
  override def execute(req: AuthenticationRequestModel, context: UseCaseContext): ResultOld[AuthenticationResponseModel] = {
    val result = new ResultOld[AuthenticationResponseModel]
    val useCaseContext = new UseCaseContextImpl();
    val user = new UserImpl
    user.id = "111111111111"
    user.firstName = "Anders"
    user.lastName = "Sätter"
    user.isAuthenticated = true
    user.isAuthorized = true
    user.email = "example@email.com"
    useCaseContext.user =user
    result.success = Option(AuthenticationResponseModel(useCaseContext))
    return result
  }
}
//class AuthenticationInteractor extends Interactor[AuthenticationRequestModel,AuthenticationResponseModel ] {
//	override def execute(req: AuthenticationRequestModel, context: UseCaseContext): AuthenticationResponseModel = {
//			val useCaseContext = new UseCaseContextImpl();
//			val user = new UserImpl
//					user.id = "111111111111"
//					user.firstName = "Anders"
//					user.isAuthenticated = true
//					user.isAuthorized = true
//					user.email = "example@email.com"
//					useCaseContext.user = new UserImpl
//					return AuthenticationResponseModel(Option(useCaseContext))
//	}
//}