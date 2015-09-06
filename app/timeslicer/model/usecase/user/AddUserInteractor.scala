package timeslicer.model.usecase.user

import timeslicer.model.interactor.OldInteractor
import timeslicer.model.framework.RequestModel
import timeslicer.model.framework.ResponseModel
import timeslicer.model.context.UseCaseContext
import timeslicer.model.storage.Storage
import timeslicer.model.storage.StorageImpl
import timeslicer.model.storage.filestorage.FileStorageUtil._

/**
 * Adds a new user to the system, and returns that user on success
 */
class AddUserInteractor extends OldInteractor[AddUserRequestModel, AddUserResponseModel] {
  
  override def execute(request:AddUserRequestModel, useCaseContext:UseCaseContext):AddUserResponseModel = {
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
    
    
    storage.addUser(request.user, useCaseContext)
    return new AddUserResponseModel(None)
  }  
}