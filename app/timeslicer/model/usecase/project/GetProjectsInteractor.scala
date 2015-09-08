package timeslicer.model.usecase.project

import timeslicer.model.framework.RequestModel
import timeslicer.model.context.UseCaseContext
import timeslicer.model.framework.ResponseModel
import timeslicer.model.project.Activity
import timeslicer.model.project.Project
import timeslicer.model.framework.Interactor
import timeslicer.model.interactor.OldInteractor
import timeslicer.model.framework.Result
import scala.util.Try
import scala.util.Failure
import scala.util.Success
import timeslicer.model.util.Util.EmptyUseCaseContext

class GetProjectsInteractor extends Interactor[GetProjectsRequestModel, GetProjectsResponseModel] {
  override def onExecute(r: GetProjectsRequestModel, u: UseCaseContext): Result[GetProjectsResponseModel] = {
    val res = new Result[GetProjectsResponseModel]
    val storage = r.storage
    Try {
      val projects = storage.projects(u).map(x => res.success = GetProjectsResponseModel(x.seq))
    } match {
      case Failure(e) => res.error = Failure(e)
      case Success(s) =>
    }
    res
  }
}