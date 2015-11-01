package timeslicer.controller.util

import timeslicer.model.autthentication.AuthenticationToken
import play.api.mvc._
import play.api.mvc.Results._
import timeslicer.model.session.Session
import timeslicer.model.autthentication.AuthenticationManager
import timeslicer.model.user.NoUser
import timeslicer.model.user.User

case class SessionUser(session: Session, user: User)
object RequestUtils {
  def getAuthenticationToken(request: Request[AnyContent]): AuthenticationToken = {
    val value = request.session.get("AuthenticationId").getOrElse("")
    AuthenticationToken("AuthenticationId", value)
  }

  
  def getSessionWithUser(request: Request[AnyContent]): Session = {
    val authManager = new AuthenticationManager
    val session = authManager.session(RequestUtils.getAuthenticationToken(request))    
    session
  }

  
  
}