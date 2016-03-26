package timeslicer.controller.util

import timeslicer.model.authentication.AuthenticationToken
import play.api.mvc._
import play.api.mvc.Results._
import timeslicer.model.session.Session
import timeslicer.model.authentication.AuthenticationManager
import timeslicer.model.user.NoUser
import timeslicer.model.user.User

/**
 * Contains the session and the user.
 * TODO remove SessionUser class if it is not used
 */
//case class SessionUser(session: Session, user: User)

/**
 * Handles users and AuthenticationToken
 */
object RequestUtils {

  /**
   * Returns the AuthenticationToken from a request or else empty string for
   * the authentication token... shouldn't it return an option instead?
   */
  def getAuthenticationTokenFromRequest(request: Request[AnyContent]): AuthenticationToken = {
    val value = request.session.get("AuthenticationId").getOrElse("")

    AuthenticationToken("AuthenticationId", value)
  }

  /**
   * Returns a session for a specific user, there is no user for the current
   * Authentication token a new token is created
   */
  def getSessionForAuthenticationToken(request: Request[AnyContent], authToken: AuthenticationToken): Session = {
    val authManager = new AuthenticationManager
    val session = authManager.session(authToken)
    session
  }
  
  /**
   * Setting the user to the session   
   */
  def setUserToSession(user:User, session:Session) = {
    session.user = user
  }

  /**
   * 1. get authentication token from the request
   * 2. get a session using the authentication token
   */
  def manageSession() = {
    
  }
  
  
  
}