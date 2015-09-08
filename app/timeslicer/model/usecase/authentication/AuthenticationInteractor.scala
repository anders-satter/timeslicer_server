package timeslicer.model.usecase.authentication

import timeslicer.model.framework.Result
import timeslicer.model.context.UseCaseContext
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.framework.Interactor
import timeslicer.model.user.UserImpl


/**
 * The use case should return a ResponseModel with a 
 * UseCaseContext that is filled with a User
 */

class AuthenticationInteractor extends Interactor[AuthenticationRequestModel,AuthenticationResponseModel]  {
  
  override def onExecute(req: AuthenticationRequestModel, context: UseCaseContext): Result[AuthenticationResponseModel] = {
    
    val result = new Result[AuthenticationResponseModel]
    val useCaseContext = new UseCaseContextImpl();
    val user = new UserImpl
    user.id = "111111111111"
    user.firstName = "Anders"
    user.lastName = "Sätter"
    user.isAuthenticated = true
    user.isAuthorized = true
    user.email = "example@email.com"
    useCaseContext.user =user
    result.success = AuthenticationResponseModel(useCaseContext) 
    return result
  }
}
//class AuthenticationInteractor extends XInteractor[AuthenticationRequestModel,ResultOld[AuthenticationResponseModel] ] {
//	override def execute(req: AuthenticationRequestModel, context: UseCaseContext): ResultOld[AuthenticationResponseModel] = {
//			val result = new ResultOld[AuthenticationResponseModel]
//					val useCaseContext = new UseCaseContextImpl();
//			val user = new UserImpl
//					user.id = "111111111111"
//					user.firstName = "Anders"
//					user.lastName = "Sätter"
//					user.isAuthenticated = true
//					user.isAuthorized = true
//					user.email = "example@email.com"
//					useCaseContext.user =user
//					result.success = Option(AuthenticationResponseModel(useCaseContext))
//					return result
//	}
//}
