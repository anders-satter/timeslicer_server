package timeslicer.controller.project

import play.api.mvc.Controller
import play.api.mvc.Action
import timeslicer.model.usecase.project.GetProjectsRequestModel
import timeslicer.model.usecase.project.GetProjectsInteractor
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.user.UserImpl
import timeslicer.model.usecase.project.GetProjectsResponseModel
import timeslicer.model.util.JsonHelper
import timeslicer.model.project.Project
import timeslicer.model.autthentication.AuthenticationManager
import timeslicer.controller.util.RequestUtils

class ProjectController extends Controller {
  def projects = Action {
    request =>
      {
        /*
         * 1. Get logged in user
         * 2. If the user is not logged in, make her log in
         * Should this been done in an interaction?
         * The user needs to be logged in from the client
         * to, so there needs to be an authentication interaction 
         * 
         */
                  
        
        //request.session.get("id").map(x => println("session value " + x))
        val authManager = new AuthenticationManager
        val session = authManager.session(RequestUtils.getAuthenticationToken(request))
        
        
        val reqModel = GetProjectsRequestModel()
        val interactor = new GetProjectsInteractor
        val useCaseContext = new UseCaseContextImpl
        val user = new UserImpl
        user.firstName = "Anders"
        user.lastName = "Sätter"
        user.id = "111111111111"
        user.email = "anders.satter@users.com"

        useCaseContext.user = user

        
        val list:Seq[Project] = interactor
          .execute(reqModel, useCaseContext)
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