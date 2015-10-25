package timeslicer.model.session.impl

import timeslicer.model.session.SessionManager
import timeslicer.model.session.Session
import timeslicer.model.session.SessionStorage
import timeslicer.model.user.NoUser
import timeslicer.exception.TimeslicerException
import timeslicer.model.util.StringIdGenerator

object SessionManagerImpl extends SessionManager {

  private[this] val sessionStorage: SessionStorage = SessionStorageImpl()

  /**
   * This must always return a Session
   */
  override def session(key: String): Session = {
    sessionStorage.get(key) match {
      case Some(session) => session
      case None => {
        val key = StringIdGenerator.sessionStorageKey()
        sessionStorage.add(new SessionImpl, key)
        sessionStorage.get(key) match {
          case Some(session) => {           
        	  session.user = NoUser
        	  session.id = key
        	  session
          }
          case None => throw new TimeslicerException("Session could not be creted, fatal error")
        }
      }
    }
  }
  
  override def sessionExists(key:String):Boolean = {
    sessionStorage.get(key) match {
      case Some(s) => true
      case None => false
    }
  } 
}