package timeslicer.model.session

import timeslicer.model.user.User
import timeslicer.model.session.impl.EmptySession
import timeslicer.model.session.impl.SessionImpl
import timeslicer.model.util.StringIdGenerator
import timeslicer.exception.TimeslicerException

/**
 * Initializes the session storage
 * returns the current session
 * retrieves the user from storage
 * and puts it in the current session
 */

object SessionManager {
    /**
   * current session, if the session is not new there should be a user
   * connected to the session
   */
  /**
   * Create a session storage
   */
  private[this] val sessionStorage: SessionStorage = SessionStorage()

  /**
   * This must always return a Session, if there is none
   * for the supplied key a new one will be created.
   */
  def session(key: String): Session = {
    /**
     * storage.get(key) returns an option, so
     * we need to use storage.get(key).getOrElse
     */
    sessionStorage.get(key).getOrElse(EmptySession);
  }
  
 def createSession(user:User) = {
      val newKey = StringIdGenerator.sessionStorageKey()
      val session = new SessionImpl
      session.user = user
      sessionStorage.add(session, newKey)
      sessionStorage.get(newKey).getOrElse{
        throw new TimeslicerException("Session could not be created, fatal error")
      }
  }
  
  def sessionExists(key: String): Boolean = sessionStorage.keySet.contains(key)

}
