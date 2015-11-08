package timeslicer.model.framework.impl

import timeslicer.model.framework.InteractionIdManager
import scala.collection._
import timeslicer.model.session.SessionStorage
import timeslicer.model.session.Session

class InteractionIdManagerImpl(val sessionStorage: SessionStorage) extends InteractionIdManager {

  
  /**
   * This key map with hold a map sesssionStorageKeys and the
   * current interaction serial number
   * Each call for new interaction id should increment the
   * integer by one
   */
  private[this] val interActionKeyHolderMap: mutable.Map[String, Int] = new mutable.HashMap[String, Int]

  /**
   * we need to hook up the sessionmanager to add listeners
   */

  private[this] def onSessionCreated(session: Session) =
    interActionKeyHolderMap
      .synchronized {
        println("InteractionIdManagerImpl.onSessionCreated:" + session.id)
        interActionKeyHolderMap.put(session.id, 0)
      }
  private[this] def onSessionDestroyed(session: Session) =
    interActionKeyHolderMap
      .synchronized {        
    	  println("InteractionIdManagerImpl.onSessionDestroyed:" + session.id)
        interActionKeyHolderMap.remove(session.id)
      }

  sessionStorage.sessionListenerService.addSessionCreatedListener(onSessionCreated)
  sessionStorage.sessionListenerService.addSessionDestroyedListener(onSessionDestroyed)

  override def interactionId(sessionStorageKey: String): String = {
    interActionKeyHolderMap.synchronized {
      //println("InteractionIdManagerImpl.interactionId sessionStorageKey:" + sessionStorageKey)
      val currentValue = interActionKeyHolderMap.get(sessionStorageKey).getOrElse(0)
      //println("InteractionIdManagerImpl.interactionId currentvalue:" + currentValue)
      val newValue = currentValue + 1
      interActionKeyHolderMap += (sessionStorageKey -> newValue)
      //println("InteractionIdManagerImpl.interactionId size"+interActionKeyHolderMap.size)
      return String.valueOf(newValue)
    }
  }

}