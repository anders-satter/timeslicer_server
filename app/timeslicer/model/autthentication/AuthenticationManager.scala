package timeslicer.model.autthentication

import timeslicer.model.user.UserManagerImpl
import timeslicer.model.session.impl.SessionManagerImpl
import timeslicer.model.user.User
import timeslicer.model.user.NoUser
import timeslicer.model.session.impl.EmptySession
import timeslicer.model.session.Session

class AuthenticationManager {
  def session(token: AuthenticationToken): Session = {
    val sessionManager = SessionManagerImpl
    return sessionManager
      .session(token.value)
  }
  
  def isUserAuthenticated(user: User): Boolean = {
    user.isAuthenticated
  }
}