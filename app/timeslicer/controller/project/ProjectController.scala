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

/**
 * What is it that this controller should be doing?
 * This controller shows all the projects for a user...
 */
class ProjectController extends Controller {
  def projects = Action {
    request =>
      {

        val reqModel = GetProjectsRequestModel()
        val interactor = new GetProjectsInteractor
        val useCaseContext = new UseCaseContextImpl


        def authenticateAction: Function0[Int] = () => {
          println("running the authenticate action")
          val result: Int = 24
          result
        }

        val frontController = FrontControllerFactory.create(authenticateAction)
        val authenticationToken =  RequestUtils.getAuthenticationTokenFromRequest(request)
        
        
        val result = frontController.perform(authenticationToken,reqModel, interactor)
        
        val list: Seq[Project] = 
          result.success
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