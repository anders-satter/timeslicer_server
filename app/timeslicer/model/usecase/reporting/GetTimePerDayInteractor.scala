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
    
    val intervalPrjActStruct = ru.projectActivityStructure(timeslices, true)
    //intervalPrjActStruct foreach println
    val projActCombinations = intervalPrjActStruct.map(p => p.activities.map(a => p.name + "|" +a.name))
    projActCombinations foreach println
    
    /*find the prjact combination in the structure*/
    
    /*
     * run a structure for the day
     * search for each prjAct combination in the structure, if not found
     * set it to 0
     * 
     */
    
    
    dayList.foreach(day => {
      val currentDayList = ru.projectActivityStructure(timeslices.filter(x => x.startdate == day))
      

      //      val sortedProjectList = currentDayList.sortBy(p => p.name)
      //      val sortedPjrAndActivitiesList = sortedProjectList.map(p => {
      //        p.activities.sortBy(a => a.name).map(a => {
      //          day + "|" +p.name + "|" + a.name + "|" + a.duration 
      //        }) 
      //      }) foreach println
      val list = for {
        p <- currentDayList.sortBy(p => p.name)
        a <- p.activities
      } yield p.name + a.name
      //list foreach println
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