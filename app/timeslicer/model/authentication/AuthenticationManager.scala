package timeslicer.model.authentication

import timeslicer.model.user.UserManagerImpl
import timeslicer.model.user.User
import timeslicer.model.user.NoUser
import timeslicer.model.session.impl.EmptySession
import timeslicer.model.session.Session
import timeslicer.model.session.SessionManager

class AuthenticationManager {
  def session(token: AuthenticationToken): Session = {
    val sessionManager = SessionManager
    return sessionManager
      .session(token.value)
  }
  
  def isUserAuthenticated(user: User): Boolean = {
    user.isAuthenticated
  }
}