package timeslicer.model.session.mapstorage

import timeslicer.model.session.SessionTimeoutManager
import timeslicer.model.session.Session
import scala.concurrent.duration.FiniteDuration
import scala.collection.mutable.HashMap
import timeslicer.model.session.SessionStorageProperties
import timeslicer.model.session.SessionStorage
import scala.util.Random
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._
import timeslicer.model.session.impl.SessionImpl
import timeslicer.model.util.settings.Settings
import timeslicer.model.session.impl.SessionStoragePropertiesImpl
import timeslicer.model.session.SessionListenerService
import timeslicer.model.session.impl.SessionListenerServiceImpl
import timeslicer.model.session.impl.EmptySession

class SessionMapStorage(val timeoutManager: SessionTimeoutManager,
                        val sessionListenerService: SessionListenerService,
                        val properties: SessionStorageProperties) extends SessionStorage {

  private[this] val map = new HashMap[String, Session]

  def inactivityTimeoutDelay: FiniteDuration = Duration(properties.inactivityTimeoutDelay, MILLISECONDS)

  /**
   * Used only for testing
   */

  override def add(session: Session, key: String): Boolean = map.synchronized {
    println("adding session with key:" + key)
    if (!map.keySet.contains(key)) {
      session.id = key
      map += (key -> session)      
      timeoutManager.handleTimeoutHandler(this, key, inactivityTimeoutDelay)
      sessionListenerService.create(session)
      true
    } else {
      false
    }
  }

  override def remove(key: String): Boolean =
    map.synchronized {
      println("cache remove called for key:" + key)
      if (map.keySet.contains(key)) {
        val removedSession = map.remove(key)        
        sessionListenerService.destroy(removedSession.getOrElse(EmptySession))
        timeoutManager.deleteTimeoutHandler(this, key)
        println("current cache size:" + this.numberOfSessions)
        true
      } else {
        false
      }
    }

  override def get(key: String): Option[Session] = {
    timeoutManager.handleTimeoutHandler(this, key, inactivityTimeoutDelay)
    map.get(key)
  }

  /**
   * Retreives a session from the system without resetting the
   * users timeout clock
   */
  override def systemGet(key: String): Option[Session] = map.get(key)
  override def numberOfSessions = map.size
  override def keySet: scala.collection.Set[String] = map.keySet

}

/**
 * Used for testing
 */
object SessionMapStorage {

  val properties = new SessionStorageProperties {
    override def inactivityTimeoutDelay: Long = 20000
  }

  val cache = new SessionMapStorage(new ActorBasedSessionTimeoutManager, new SessionListenerServiceImpl,properties)

  (10 to 99).foreach { i =>
    val session = new SessionImpl
    session.id = "0000000key" + i
    cache.add(session, "0000000key" + i)
    Thread.sleep(Random.nextInt(500))
  }

  def main(args: Array[String]): Unit = {}

}