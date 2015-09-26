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

    //storage.timeslices(request.startday, request.endday, useCaseContext).get foreach println

    /*
     *  we are going to return a 2-dim Seq that shows the above structure
     *  and we assume the seq of timeslices returned from storage
     *  is sorted on startdate
     */

    val dayList = dt.getDayList(request.startday, request.endday)
    val timeslices = storage.timeslices(request.startday, request.endday, useCaseContext).getOrElse(Seq())
    dayList.foreach(day => {
       //ru.summarizeSelection(timeslices.filter(x=>x.startdate==day)).map(sp => sp.duration) foreach println
      val dailyList = ru.summarizeSelection(timeslices.filter(x=>x.startdate==day))
      //dailyList foreach println
      dailyList.foreach(p => {        
        println(p.duration)        
      })
    })

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