package timeslicer.model.session

import timeslicer.model.session.mapstorage.SessionMapStorage
import timeslicer.model.session.mapstorage.ActorBasedSessionTimeoutManager
import timeslicer.model.session.impl.SessionListenerServiceImpl
import timeslicer.model.session.impl.SessionStoragePropertiesImpl

trait SessionStorage {
  
   /**
   * Adds session to the session storage with its key
   */
  def add(session: Session, key: String): Boolean
  
  /**
   * Returns an option to the session stored with the
   * supplied key
   */
  def get(key: String): Option[Session]

  /**
   * Returns an option to the session stored with the
   * supplied key
   */  
  def remove(key: String): Boolean
  def numberOfSessions:Long
  def keySet:scala.collection.Set[String]
  /**
   * returns the session without 'touching' it, 
   * so timeout mechanism should not be reset.
   * This getter should only be used for monitoring
   * and internal purposes
   */
  def systemGet(key:String): Option[Session]
  
  def properties:SessionStorageProperties
  
  def sessionListenerService:SessionListenerService
}

/**
 * This is the standard implementation of session storage
 */
object SessionStorage {
    val implementation: SessionStorage = new SessionMapStorage(new ActorBasedSessionTimeoutManager,
    new SessionListenerServiceImpl,
    new SessionStoragePropertiesImpl)

  def apply(): SessionStorage = {
    implementation
  }
}


