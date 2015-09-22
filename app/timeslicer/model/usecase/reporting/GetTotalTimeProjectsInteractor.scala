package timeslicer.model.usecase.reporting

import timeslicer.model.framework.Interactor
import timeslicer.model.framework.Result
import timeslicer.model.context.UseCaseContext
import timeslicer.model.storage.filestorage.FileStorage
import scala.collection.mutable.ListBuffer
import timeslicer.model.reporting.SumProject
import timeslicer.model.reporting.SumActivity
import timeslicer.model.timeslice.TimeSlice
import timeslicer.model.reporting.SumActivity
import timeslicer.model.reporting.SumActivity
import timeslicer.model.util.{ DateTime => dt, Util => u }
import timeslicer.model.reporting.TotalResultStructure
import scala.util.Try
import scala.util.Failure
import scala.util.Success


class GetTotalTimeProjectsInteractor extends Interactor[GetTotalTimeProjectsRequestModel, GetTotalTimeProjectsResponseModel] {
  override def onExecute(request: GetTotalTimeProjectsRequestModel, useCaseContext: UseCaseContext) = {
    val result = new Result[GetTotalTimeProjectsResponseModel]
    val slices = storage.timeslices(request.startday, request.endday, useCaseContext).getOrElse(Seq())
    val sortedOnProjects = slices.sortBy(item => item.project)
    val projectMap = sortedOnProjects.groupBy { x => x.project }

    val projectList: ListBuffer[SumProject] = new ListBuffer()
    projectMap.foreach(x => {
      val prjName = x._1
      val sortedActList = x._2.sortBy(i => i.activity)
      val activityMap = sortedActList.groupBy(item => item.activity)
      val activityList: ListBuffer[SumActivity] = new ListBuffer()

      activityMap.foreach { item =>
        activityList += SumActivity(item._1, item._2.toSeq)
      }
      projectList += SumProject(x._1, activityList.toSeq)
    })    
    Try {    	
    	result.success = GetTotalTimeProjectsResponseModel(TotalResultStructure(request.startday, request.endday, projectList))    
    } match {
      case Failure(e) => result.error = Failure(e)
      case Success(c) =>
    }
    result
  }
}
