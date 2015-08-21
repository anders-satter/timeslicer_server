package timeslicer.model.usecase.project

import timeslicer.model.interactor.Interactor
import timeslicer.model.api.RequestModel
import timeslicer.model.context.UseCaseContext
import timeslicer.model.api.ResponseModel
import timeslicer.model.project.Activity
import timeslicer.model.project.Project


class GetProjectsInteractor extends Interactor[
  GetProjectsRequestModel,
  GetProjectsResponseModel] {
  override def execute(request:GetProjectsRequestModel, useCaseContext:UseCaseContext):GetProjectsResponseModel = {
    val storage = request.asInstanceOf[GetProjectsRequestModel].storage
    val projectList = storage.projects(useCaseContext).get    
    
    return GetProjectsResponseModel(projectList)
  }
}