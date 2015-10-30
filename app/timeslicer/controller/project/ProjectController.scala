package timeslicer.controller.project

import play.api.mvc.Controller
import play.api.mvc.Action
import timeslicer.model.usecase.project.GetProjectsRequestModel
import timeslicer.model.usecase.project.GetProjectsInteractor
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.user.UserImpl
import timeslicer.model.user.User
import timeslicer.model.usecase.project.GetProjectsResponseModel
import timeslicer.model.util.JsonHelper
import timeslicer.model.project.Project
import timeslicer.model.autthentication.AuthenticationManager
import timeslicer.controller.util.RequestUtils
import timeslicer.model.user.NoUser

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
        
        val session = RequestUtils.getSessionWithUser(request)
        val user = session.user.getOrElse{
          throw new IllegalStateException("Session and user creation failed")
        }
        
        if (!session.user.get.isAuthenticated) {
          /*If the user is not authenticated with return an http 401 and the session id*/
          Unauthorized("User is not authorized\n").withSession("AuthenticationId" -> session.id)
        } else {

          val reqModel = GetProjectsRequestModel()
          val interactor = new GetProjectsInteractor
          val useCaseContext = new UseCaseContextImpl

          useCaseContext.user = user

          val list: Seq[Project] = interactor
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
}