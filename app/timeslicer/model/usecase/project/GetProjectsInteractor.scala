package timeslicer.model.usecase.project

import timeslicer.model.interactor.Interactor
import timeslicer.model.api.RequestModel
import timeslicer.model.context.UseCaseContext
import timeslicer.model.api.ResponseModel
import timeslicer.model.project.Activity
import timeslicer.model.project.Project


class GetProjectsInteractor extends Interactor {
  override def execute(request:RequestModel, useCaseContext:UseCaseContext):ResponseModel = {
    val storage = request.asInstanceOf[GetProjectsRequestModel].storage
    val projectList = storage.projects(useCaseContext).get    
    
    return GetProjectsResponseModel(projectList)
  }
}