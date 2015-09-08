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

/**
 * Adds a new user to the system, and returns that user on success
 */
class AddUserInteractor extends Interactor[AddUserRequestModel, AddUserResponseModel] {
  
  override def onExecute(request:AddUserRequestModel, useCaseContext:UseCaseContext):Result[AddUserResponseModel] = {
    
    val result = new Result[AddUserResponseModel]
    val storage = StorageImpl()
    
    /*
     * first check if the user already exists
     * we will do this on the by checking the names
     * and the email
     */
     val currentUserList = (storage.users() match {
       case Some(l) => l
       case None => Seq()
     })
     
     if(matchesUserName(currentUserList, request.user)){
       
     }
    
    Try {
      storage.addUser(request.user)
    	result.success = AddUserResponseModel(Option(request.user))
    } match {
      case Failure(e) => result.error = Failure(e)
      case Success(u) =>
    }
    result
    
  }  
}