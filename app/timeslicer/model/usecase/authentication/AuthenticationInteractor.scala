package timeslicer.model.usecase.authentication

import timeslicer.model.context.UseCaseContext
import timeslicer.model.framework.Interactor
import timeslicer.model.framework.Result
import timeslicer.model.user.NoUser
import timeslicer.model.user.PasswordUtil
import timeslicer.model.user.User

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

    //println("Running AuthenticationInteractor")
    val result = new Result[AuthenticationResponseModel]
    //println("STORAGE:" +storage)
     try { 
    	 val u = storage.users();
   } catch {
     case e: Exception => println(e)
   }
    //println("UUUUU" + u)
    val users = storage.users().getOrElse(Seq()).toList
    //println("Users:" + users)
    val currentUserList = for {
      user <- users;
      if (user.userName == req.userName.getOrElse("") || user.email.getOrElse("") == req.email.getOrElse(""))
    } yield user
    
    val foundUser = if (!currentUserList.isEmpty) currentUserList.head else NoUser
    //println("foundUser:" + foundUser)

    if (foundUser != NoUser && isPasswordCorrect(req, foundUser)) {
      useCaseContext.user = foundUser
      result.success = AuthenticationResponseModel(foundUser)
    } else {
      result.failure = "User or password incorrect"
    }
    return result
  }

  /**
   * Checks the password against the stored password
   */
  def isPasswordCorrect(req: AuthenticationRequestModel, foundUser: User) = { /* 
    	 * hash the password and check it against the stored password hash
    	 */
    val testHash = PasswordUtil.createHash(req.password, foundUser.passwordSalt)

    if (testHash == foundUser.passwordHash) {
      foundUser.isAuthenticated = true
      true
    } else {
      false
    }
  }
}
