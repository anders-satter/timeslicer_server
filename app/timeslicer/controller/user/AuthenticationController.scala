package timeslicer.controller.user

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.mvc.Request
import timeslicer.model.usecase.authentication.AuthenticationRequestModel

/**
 * Authenticates the user
 * If the user already called another rest service there is already a
 * user and a session.
 * If not we will create a session and a user.
 */
class AuthenticationController extends Controller {
  def user = Action { request =>
    {
      val req = AuthenticationRequestModel
      
      //println(request.)

      Ok(""" {"answer":"Hello"} """)
    }
  }

}