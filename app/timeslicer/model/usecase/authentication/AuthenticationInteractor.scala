package timeslicer.model.usecase.authentication

import timeslicer.model.framework.Result
import timeslicer.model.context.UseCaseContext
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.framework.Interactor
import timeslicer.model.user.UserImpl
import timeslicer.model.user.NoUser
import timeslicer.model.user.PasswordHashCaclulator

/**
 *
 * This should return an authenticated user
 *
 */
class AuthenticationInteractor extends Interactor[AuthenticationRequestModel, AuthenticationResponseModel] {

  override def onExecute(req: AuthenticationRequestModel): Result[AuthenticationResponseModel] = {

    val result = new Result[AuthenticationResponseModel]
    /*
     * get the user from the user list
     */
    val users = storage.users()
    val currentUser = users.getOrElse(Seq(NoUser).toList)
      .toList
      .filter(u => u.userName == req.userName.getOrElse("") 
          || req.email.getOrElse("") == u.email)
      
      result.success = AuthenticationResponseModel(currentUser(0))
    
    /* 
     * hash the password and check it against the stored password hash
     */
     val testHash = PasswordHashCaclulator.createHash(req.password, currentUser(0).passwordSalt)
    
     if (testHash == currentUser(0).passwordHash){
       currentUser(0).isAuthenticated = true
     }
    
    
    
    /*
     * if the user exists, and the password exists, set the user as authenticated 
     */

    /*
     * fail path:
     * 1) user does not exist -> 
     * 2) password is incorrect ->
     */

    //    val useCaseContext = new UseCaseContextImpl();
    //    val user = new UserImpl
    //    user.id = "111111111111"
    //    user.firstName = "Anders"
    //    user.lastName = "Sätter"
    //    user.isAuthenticated = true
    //    user.isAuthorized = true
    //    user.email = "example@email.com"
    //    useCaseContext.user = user
    //    result.success = AuthenticationResponseModel(useCaseContext)
    
    return result
  }
}
