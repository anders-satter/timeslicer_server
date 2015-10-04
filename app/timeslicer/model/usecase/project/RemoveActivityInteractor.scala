package timeslicer.model.usecase.project

import timeslicer.model.framework.Interactor
import timeslicer.model.context.UseCaseContext
import timeslicer.model.framework.Result
import scala.util.Failure

class RemoveActivityInteractor extends Interactor[RemoveActivityRequestModel, RemoveActivityResponseModel] {
  override def onExecute(request: RemoveActivityRequestModel, useCaseContext: UseCaseContext): Result[RemoveActivityResponseModel] = {
    val result = new Result[RemoveActivityResponseModel]

    storage.removeActivity(request.project, request.activity, useCaseContext) match {
      case Right(r) => result.success = RemoveActivityResponseModel(true)
      case Left(l) => {
        result.success = RemoveActivityResponseModel(false)
        result.error = Failure(l.exception.getOrElse(new Exception("Unknown error in RemoveProjectInteractor")))
      }
    }
    result
  }
}