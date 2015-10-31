package timeslicer.model.usecase.reporting

import timeslicer.model.framework.Interactor
import timeslicer.model.context.UseCaseContext
import timeslicer.model.framework.Result
import timeslicer.model.reporting.DailyResultStructure
import scala.util.Try
import scala.util.Failure
import scala.util.Success
import timeslicer.model.util.{ DateTime => dt }
import timeslicer.model.reporting.{ ReportingUtil => ru }
import timeslicer.model.reporting.ProjectActivityDayResult

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

    val dayList = dt.getDayList(request.startday, request.endday)
    val timeslices = storage.timeslices(request.startday, request.endday, useCaseContext).getOrElse(Seq())
    val intervalPrjActStruct = ru.projectActivityStructure(timeslices, true)
    
    /*find the project/activity combinations in the structure*/
    val projActCombinations = ru.projectActivityCombinations(intervalPrjActStruct)
    
    val byDayListMap = dayList.map(day => {
      /*create a compilation for this day*/
      val struct = ru.projectActivityStructure(timeslices.filter(x => x.startdate == day))
      projActCombinations.map(combo => {
        /*create result for this day for each project/activity combination*/
        ProjectActivityDayResult(day, combo._1, combo._2, ru.getDuration(combo._1, combo._2, struct))
      })
        /*create map structure with day as the key*/
        .groupBy(_.day)
    })

    Try {
      result.success = GetTimePerDayResponseModel(DailyResultStructure(request.startday, request.endday, byDayListMap))
    } match {
      case Failure(e) => result.error = Failure(e)
      case Success(s) =>
    }
    result
  }
}