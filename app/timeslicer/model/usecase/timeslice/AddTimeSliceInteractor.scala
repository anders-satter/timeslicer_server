package timeslicer.model.usecase.timeslice

import timeslicer.model.framework.Interactor
import timeslicer.model.usecase.reporting.GetTimePerDayRequestModel
import timeslicer.model.context.UseCaseContext
import timeslicer.model.usecase.reporting.GetTimePerDayResponseModel
import timeslicer.model.framework.Result
import scala.util.Try
import timeslicer.model.usecase.reporting.GetTimePerDayResponseModel
import scala.util.Success
import scala.util.Failure
import timeslicer.model.storage.StorageFailResult
import scala.util.Failure

class AddTimeSliceInteractor extends Interactor[AddTimeSliceRequestModel, AddTimeSliceResponseModel] {
  override def onExecute(request: AddTimeSliceRequestModel,
                         useCaseContext: UseCaseContext): Result[AddTimeSliceResponseModel] = {
    val result = new Result[AddTimeSliceResponseModel]
    storage.addTimeSlice(request.timeslice, useCaseContext) match {
      case Right(r) => result.success = AddTimeSliceResponseModel(true)
      case Left(l)  => result.error = Failure(l.exception.getOrElse(new Exception("No exception provided from AddTimeSliceInteractor")))
    }
    result
  }
}