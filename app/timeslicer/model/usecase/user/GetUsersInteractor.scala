package timeslicer.model.usecase.user

import timeslicer.model.framework.Interactor
import timeslicer.model.context.UseCaseContext
import timeslicer.model.framework.Result
import scala.util.Try
import scala.util.Success
import scala.util.Failure

class GetUsersInteractor extends Interactor[GetUsersRequestModel, GetUsersResponseModel] {
  override def onExecute(request: GetUsersRequestModel, useCaseContext: UseCaseContext): Result[GetUsersResponseModel] = {
    val result = new Result[GetUsersResponseModel]
    Try {
      val storedUsers = storage.users().getOrElse(Seq())
      result.success = GetUsersResponseModel(storedUsers)
    } match {
      case Success(s) => 
      case Failure(e) => result.error = Failure(e)        
    }
    return result
  }
}