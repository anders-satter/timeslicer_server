package timeslicer.model.usecase.reporting

import scala.collection.mutable.ListBuffer
import scala.util.Failure
import scala.util.Success
import scala.util.Try

import timeslicer.model.context.UseCaseContext
import timeslicer.model.framework.Interactor
import timeslicer.model.framework.Result
import timeslicer.model.reporting.SumActivity
import timeslicer.model.reporting.SumProject
import timeslicer.model.reporting.TotalResultStructure
import timeslicer.model.reporting.{ ReportingUtil => ru }

/**
 * Returns a summary structure for the time interval which 
 * can be presented like this
 *
 * Prj1 10 (48%)
 *  -> Act1 6 (60%) (29%)
 *  -> Act2 4 (40%)
 * Prj2 11 52%
 *  -> Act1 11 (100%) (52%)
 * Sum  21 100%
 * 
 * But actually only returns structure of project
 * 
 */
class GetTotalTimeProjectsInteractor extends Interactor[GetTotalTimeProjectsRequestModel, GetTotalTimeProjectsResponseModel] {
  override def onExecute(request: GetTotalTimeProjectsRequestModel, useCaseContext: UseCaseContext) = {
    val result = new Result[GetTotalTimeProjectsResponseModel]

    val slices = storage.timeslices(request.startday, request.endday, useCaseContext).getOrElse(Seq())

    val projectList = ru.summarizeSelection(slices)

    Try {
      result.success = GetTotalTimeProjectsResponseModel(TotalResultStructure(request.startday, request.endday, projectList))
    } match {
      case Failure(e) => result.error = Failure(e)
      case Success(c) =>
    }
    result
  }
}
