package timeslicer.controller

import play.api.mvc.ActionBuilder
import play.api.mvc.Request
import scala.concurrent.Future
import play.api.libs.json.JsObject
import play.api.mvc.Result
import play.api.libs.json.Json
import play.api.mvc.Results
import play.api.mvc.Results.Status
import timeslicer.model.session.SessionManager

object AuthenticatedAction extends ActionBuilder[Request] {
  def invokeBlock[A](request: Request[A],
                     block: (Request[A]) => Future[Result]) = {
    
    val authId = request.session.get("AuthenticationId").getOrElse("")
    if (!authId.equals("") && SessionManager.sessionExists(authId)) {
      //println("SESSION EXISTS - RUNNING THE BLOCK")
      block(request)
    } else {      
    	//println("SESSION DOES NOT EXIST - RETURNING FORBIDDEN")
      Future.successful(Results.Unauthorized)
    }
    
  }
}