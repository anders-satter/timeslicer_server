package timeslicer.controller.user

import play.api.mvc.Action
import play.api.mvc.Controller
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.usecase.user.GetUsersInteractor
import timeslicer.model.usecase.user.GetUsersRequestModel
import timeslicer.model.usecase.user.GetUsersResponseModel
import timeslicer.model.user.UserImpl
import timeslicer.model.util.JsonHelper

/**
 * Will return the user.
 */
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

      val list = interactor
        .execute(reqModel, useCaseContext)
        .success
        .getOrElse(GetUsersResponseModel(Seq()))
        .userList

      if (list.nonEmpty) {

        Ok(JsonHelper.jsonUserList(list))
      } else {
        Ok("{}")
      }
    }
  }
}

