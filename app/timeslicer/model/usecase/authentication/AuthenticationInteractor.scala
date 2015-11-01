package timeslicer.model.usecase.authentication

import timeslicer.model.framework.Result
import timeslicer.model.context.UseCaseContext
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.framework.Interactor
import timeslicer.model.user.UserImpl
import timeslicer.model.user.NoUser
import timeslicer.model.user.PasswordUtil

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

  override def onExecute(req: AuthenticationRequestModel, useCaseContext: UseCaseContext): Result[AuthenticationResponseModel] = {

    val result = new Result[AuthenticationResponseModel]

    
    val users = storage.users().getOrElse(Seq()).toList
    val currentUserList = for {
      user <- users
      if (user.userName == req.userName.getOrElse("") || user.email.getOrElse("") == req.email.getOrElse(""))
    } yield user

    val foundUser = if (!currentUserList.isEmpty) currentUserList.head else NoUser

    result.success = AuthenticationResponseModel(foundUser)

    if (foundUser != NoUser) {
      /* 
    	 * hash the password and check it against the stored password hash
    	 */
      val testHash = PasswordUtil.createHash(req.password, foundUser.passwordSalt)

      if (testHash == foundUser.passwordHash) {
        foundUser.isAuthenticated = true
      }
    }
    return result
  }
}
