package timeslicer.model.usecase.user

import timeslicer.model.interactor.Interactor
import timeslicer.model.api.RequestModel
import timeslicer.model.api.ResponseModel
import timeslicer.model.context.UseCaseContext
import timeslicer.model.storage.Storage
import timeslicer.model.storage.StorageImpl

class AddUserInteractor extends Interactor[AddUserRequestModel, AddUserResponseModel] {
  
  override def execute(request:AddUserRequestModel, useCaseContext:UseCaseContext):AddUserResponseModel = {
    val storage = StorageImpl()
    storage.addUser(request.asInstanceOf[AddUserRequestModel].user, useCaseContext)
    return new AddUserResponseModel
  }  
}