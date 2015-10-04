package timeslicer.model.usecase.project

import timeslicer.model.framework.Interactor
import timeslicer.model.context.UseCaseContext
import timeslicer.model.framework.Result
import scala.util.Failure

class AddProjectInteractor extends Interactor[AddProjectRequestModel, AddProjectResponseModel]{
  override def onExecute(request: AddProjectRequestModel, useCaseContext:UseCaseContext):
  Result[AddProjectResponseModel] = {
    val result = new Result[AddProjectResponseModel]
    
    storage.addProject(request.project, useCaseContext) match{
      case Right(r) => result.success = AddProjectResponseModel(true)
      case Left(l) => {
       result.success =  AddProjectResponseModel(false)
       result.error = 
         Failure(l.exception
             .getOrElse(new Exception("No exception provided from AddTimeSliceInteractor"))) 
      } 
    }       
    return result
  }
}