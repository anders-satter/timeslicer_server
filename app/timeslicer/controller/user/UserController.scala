package timeslicer.controller.user

import play.api.mvc.Controller
import play.api.mvc.Action
import timeslicer.model.usecase.user.GetUsersInteractor
import timeslicer.model.usecase.user.GetUsersRequestModel
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.user.UserImpl

class UserController extends Controller {
  
  def users = Action { request =>
    {
      val reqModel = GetUsersRequestModel()

      val interactor = new GetUsersInteractor
      val useCaseContext = new UseCaseContextImpl
      val user = new UserImpl
      user.firstName = "Anders"
      user.lastName = "Sätter"
      user.id = "111111111111"
      user.email = "anders.satter@users.com"

      useCaseContext.user = user

      interactor.execute(reqModel, useCaseContext) match {
        case success => {
          Ok("")
        }
        case error => {
          Ok("")

        }
      }
      NotFound("" + request.cookies)
    }
  }

}