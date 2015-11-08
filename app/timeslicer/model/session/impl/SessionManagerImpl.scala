package timeslicer.model.session.impl

import timeslicer.model.session.SessionManager
import timeslicer.model.session.Session
import timeslicer.model.session.SessionStorage
import timeslicer.model.user.NoUser
import timeslicer.exception.TimeslicerException
import timeslicer.model.util.StringIdGenerator

object SessionManagerImpl extends SessionManager {

  private[this] val sessionStorage: SessionStorage = SessionStorage()

  /**
   * This must always return a Session
   */
  override def session(key: String): Session = {
    /**
     * storage.get(key) returns an option, so
     * we need to use storage.get(key).getOrElse
     */
    sessionStorage.get(key).getOrElse {
      val newKey = StringIdGenerator.sessionStorageKey()
      sessionStorage.add(new SessionImpl, newKey)
      sessionStorage.get(newKey).getOrElse{
        throw new TimeslicerException("Session could not be creted, fatal error")
      }
    }    
  }

  override def sessionExists(key: String): Boolean = sessionStorage.keySet.contains(key)

}