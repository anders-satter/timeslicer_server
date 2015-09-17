package timeslicer.model.usecase.reporting

import timeslicer.model.framework.Interactor
import timeslicer.model.framework.Result
import timeslicer.model.context.UseCaseContext
import timeslicer.model.storage.filestorage.FileStorage

class GetTotalTimeProjectsInteractor extends Interactor[GetTotalTimeProjectsRequestModel, GetTotalTimeProjectsResponseModel] {
  override def onExecute(request:GetTotalTimeProjectsRequestModel, useCaseContext:UseCaseContext) = {
    val result = new Result[GetTotalTimeProjectsResponseModel]
    
    storage.timeslices(request.startday, request.endday, useCaseContext).getOrElse(Seq()) foreach println    
    result
  }
}