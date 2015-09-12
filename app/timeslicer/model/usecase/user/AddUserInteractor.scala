package timeslicer.model.usecase.user

import timeslicer.model.framework.RequestModel
import timeslicer.model.framework.ResponseModel
import timeslicer.model.context.UseCaseContext
import timeslicer.model.storage.Storage
import timeslicer.model.storage.StorageImpl
import timeslicer.model.storage.filestorage.FileStorageUtil._
import timeslicer.model.framework.Interactor
import timeslicer.model.framework.Result
import scala.util.Try
import scala.util.Failure
import scala.util.Success
import timeslicer.model.user.User
import timeslicer.model.util.{ Util => u }
import timeslicer.model.storage.exception.ItemAlreadyExistsException

/**
 * Adds a new user to the system, and returns that user on success
 */
class AddUserInteractor extends Interactor[AddUserRequestModel, AddUserResponseModel] {

  override def onExecute(request: AddUserRequestModel, useCaseContext: UseCaseContext): Result[AddUserResponseModel] = {

    val result = new Result[AddUserResponseModel]
    val currentUserList: Seq[User] = storage.users().getOrElse(Seq())
    if (u.matchesUserName(currentUserList, request.user)) {
      result.error = Failure(new ItemAlreadyExistsException("User name already exists"))
      return result
    }

    if (u.matchesEmail(currentUserList, request.user)) {
      result.error = Failure(new ItemAlreadyExistsException("Email already exists for a registered user"))
      return result
    }

    Try {
      storage.addUser(request.user)
      result.success = AddUserResponseModel(Option(request.user))
    } match {
      case Failure(e) => result.error = Failure(e)
      case Success(u) => /*should never come here*/
    }
    result

  }
}