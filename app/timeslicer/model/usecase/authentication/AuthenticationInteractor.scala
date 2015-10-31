package timeslicer.model.usecase.authentication

import timeslicer.model.framework.Result
import timeslicer.model.context.UseCaseContext
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.framework.Interactor
import timeslicer.model.user.UserImpl
import timeslicer.model.user.NoUser
import timeslicer.model.user.PasswordHashCaclulator

/**
 * This interactor returns true if
 * 1) the user exists in storage (matching on either userName or email)
 * 2) password matches
 *
 * If either condition is not fullfilled, it will return
 * false. The execution should never fail, since we are
 * always falling back to NoUser user implementation
 */
class AuthenticationInteractor extends Interactor[AuthenticationRequestModel, AuthenticationResponseModel] {

  override def onExecute(req: AuthenticationRequestModel, useCaseContext:UseCaseContext): Result[AuthenticationResponseModel] = {

    val result = new Result[AuthenticationResponseModel]
    /*
     * get the user from the user list
     */
    val users = storage.users()
    val currentUser = users.getOrElse(Seq(NoUser).toList)
      .toList
      .filter(u => u.userName == req.userName.getOrElse("")
        || req.email.getOrElse("") == u.email)
      .head

    result.success = AuthenticationResponseModel(currentUser)

    if (currentUser != NoUser) {
      /* 
    	 * hash the password and check it against the stored password hash
    	 */
      val testHash = PasswordHashCaclulator.createHash(req.password, currentUser.passwordSalt)

      if (testHash == currentUser.passwordHash) {
        currentUser.isAuthenticated = true
      }
    }
    return result
  }
}
