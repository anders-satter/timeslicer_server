package timeslicer.model.usecase.project

import timeslicer.model.framework.Result
import timeslicer.model.context.UseCaseContext
import timeslicer.model.framework.Interactor
import scala.util.Failure

class AddActivityInteractor extends Interactor[AddActivityRequestModel, AddActivityResponseModel] {
  override def onExecute(request: AddActivityRequestModel, useCaseContext: UseCaseContext): Result[AddActivityResponseModel] = {
    val result = new Result[AddActivityResponseModel]

    storage.addActivity(request.project, request.activity, useCaseContext) match {
      case Right(r) => result.success = AddActivityResponseModel(true)
      case Left(l) => {
        result.success = AddActivityResponseModel(false)
        result.error =
          Failure(l.exception
            .getOrElse(new Exception("No exception provided from " + AddActivityInteractor.className)))
      }
    }
    return result

    result
  }
}

object AddActivityInteractor {  
	private val className = AddActivityInteractor.getClass.getSimpleName		
}
