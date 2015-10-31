package timeslicer.model.usecase.project

import timeslicer.model.framework.Interactor
import timeslicer.model.context.UseCaseContext
import timeslicer.model.framework.Result
import scala.util.Failure

class RemoveProjectInteractor extends Interactor[RemoveProjectRequestModel, RemoveProjectResponseModel]{
  
  override def onExecute(request:RemoveProjectRequestModel,useCaseContext:UseCaseContext):
      Result[RemoveProjectResponseModel] = {    
    val result = new Result[RemoveProjectResponseModel]
    
    storage.removeProject(request.project, useCaseContext) match {
      case Right(r) => result.success = RemoveProjectResponseModel(true)
      case Left(l) => {
        result.success = RemoveProjectResponseModel(false)
        result.error = Failure(l.exception.getOrElse(new Exception("Unknown error in RemoveProjectInteractor")))
      }
    }
    result
  }
}