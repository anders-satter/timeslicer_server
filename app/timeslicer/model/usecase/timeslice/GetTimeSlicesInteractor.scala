package timeslicer.model.usecase.timeslice

import timeslicer.model.framework.Interactor
import timeslicer.model.framework.Result
import timeslicer.model.context.UseCaseContext
import scala.util.Try
import scala.util.Failure
import scala.util.Success

class GetTimeSlicesInteractor extends Interactor[GetTimeSlicesRequestModel, GetTimeSlicesResponseModel] {
  override def onExecute(request: GetTimeSlicesRequestModel, useCaseContext: UseCaseContext): Result[GetTimeSlicesResponseModel] = {
    
    val result = new Result[GetTimeSlicesResponseModel]
    Try {
      result.success = GetTimeSlicesResponseModel(storage
        .timeslices(request.startdate, request.enddate, useCaseContext)
        .getOrElse(Seq()))
    } match {
      case Failure(e) => result.error = Failure(e)
      case Success(s) =>
    }
    result
  }
}