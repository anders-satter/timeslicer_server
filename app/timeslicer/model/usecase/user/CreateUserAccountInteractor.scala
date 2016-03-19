package timeslicer.model.usecase.user

import timeslicer.model.framework.Interactor
import timeslicer.model.context.UseCaseContext
import timeslicer.model.framework.Result
import timeslicer.model.user.UserImpl
import timeslicer.model.user.PasswordUtil
import scala.util.Try
import scala.util.Failure

/**
 * User account creation, takes a users and checks the information
 */
class CreateUserAccountInteractor extends Interactor[CreateUserAccountRequestModel, CreateUserAccountResponseModel] {
  override def onExecute(req: CreateUserAccountRequestModel, useCaseContext: UseCaseContext): Result[CreateUserAccountResponseModel] = {
    val result = new Result[CreateUserAccountResponseModel]
    val user = new UserImpl
    println("XXUserName: " +  req.userName );
    user.userName = req.userName
    
    user.firstName = req.firstName
    println("XXFirstName: " + req.firstName);
    user.lastName = req.lastName
    user.email = req.email.getOrElse("")
    
    /**
     * Check the password
     */
    val passwordCreated = if (PasswordUtil.isPasswordValid(req.password)) {
      val salt = PasswordUtil.createSalt
      user.passwordHash = PasswordUtil.createHash(req.password, salt)
      true
    } else {
      false
    }
    result.success = CreateUserAccountResponseModel(user, passwordCreated)

    result
  }
}