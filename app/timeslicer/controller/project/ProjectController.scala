package timeslicer.controller.project

import play.api.mvc.Action
import play.api.mvc.Controller
import timeslicer.controller.util.RequestUtils
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.frontcontroller.FrontControllerFactory
import timeslicer.model.project.Project
import timeslicer.model.usecase.project.GetProjectsInteractor
import timeslicer.model.usecase.project.GetProjectsRequestModel
import timeslicer.model.usecase.project.GetProjectsResponseModel
import timeslicer.model.util.JsonHelper
import timeslicer.controller.TimeslicerController

/**
 * This controller shows all the projects for a user...
 */
class ProjectController extends TimeslicerController {

  def projects = Action {
    request =>
      {
        val reqModel = GetProjectsRequestModel()
        val interactor = new GetProjectsInteractor
        
        val result = performInteraction(request, reqModel, interactor)
        
        val list: Seq[Project] =
          result
            .success
            .getOrElse(GetProjectsResponseModel(Seq()))
            .projectList
        if (list.nonEmpty) {
          Ok(JsonHelper.jsonProjectList(list))
        } else {
          Ok("{}")
        }
      }
  }
}