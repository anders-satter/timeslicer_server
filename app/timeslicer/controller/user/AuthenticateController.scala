package timeslicer.controller.user

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.mvc.Request
import timeslicer.model.usecase.authentication.AuthenticationRequestModel

class AuthenticateController extends Controller {
  def authenticate = Action { request =>
    {
     val req = AuthenticationRequestModel      
      
      
      Ok("{}")
    }
  }

}