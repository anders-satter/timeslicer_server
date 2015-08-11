package timeslicer.model.usecase.projectlist

import timeslicer.model.interactor.Interactor
import timeslicer.model.api.RequestModel
import timeslicer.model.context.UseCaseContext
import timeslicer.model.api.ResponseModel
import timeslicer.model.project.Activity
import timeslicer.model.project.Project


class ProjectListInteractor extends Interactor {
  override def execute(request:RequestModel, useCaseContext:UseCaseContext):ResponseModel = {
    val projectList:List[Project] = List(Project("Project1"))
    return ProjectListResponseModel(projectList)
  }
}