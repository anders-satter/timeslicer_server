package timeslicer.model.usecase.user

import timeslicer.model.interactor.Interactor
import timeslicer.model.api.RequestModel
import timeslicer.model.api.ResponseModel
import timeslicer.model.context.UseCaseContext

class AddUserInteractor extends Interactor {
  override def execute(request:RequestModel, useCaseContext:UseCaseContext):ResponseModel = {
    
    return new AddUserResponseModel
  }  
}