package timeslicer.model.usecase.reporting

import timeslicer.model.framework.Interactor
import timeslicer.model.context.UseCaseContext
import timeslicer.model.framework.Result
import timeslicer.model.reporting.DailyResultStructure
import scala.util.Try
import scala.util.Failure
import scala.util.Success

/**
 * Returns a summary structure of projects and activities for each day in the time interval,
 * should facilitate a structure like this
 *
 *  Prj  Act  date1 date2 date3 date4 ... Sum
 *  ---  ---  ----- ----- ----- ----- --- ---
 *  Prj1 Act1   1     1     1     1         4
 *  Prj1 Act2   0     1     3     3         7
 *  Prj2 Act1   0     7     9     3         19
 *  ---  ---  ----- ----- ----- ----- ---  ---
 *  Sum         2     9     10    7         29
 */
class GetTimePerDayInteractor extends Interactor[GetTimePerDayRequestModel, GetTimePerDayResponseModel] {
  override def onExecute(request: GetTimePerDayRequestModel,
                         useCaseContext: UseCaseContext): Result[GetTimePerDayResponseModel] = {
    val result = new Result[GetTimePerDayResponseModel]
    val resultStructure = DailyResultStructure(request.startday, request.endday, Seq())
    
    
    Try {
      result.success = GetTimePerDayResponseModel(resultStructure)
    } match {
      case Failure(e) => result.error = Failure(e)
      case Success(s) =>
    }
    result
  }
}