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

class SessionMapStorage(val timeoutManager: SessionTimeoutManager,
                        inactivityTimeoutFunc: FiniteDuration) extends SessionStorage {

  private[this] val map = new HashMap[String, Session]
  def properties: SessionStorageProperties = new SessionStorageProperties {
    def inactivityTimeoutDelay = inactivityTimeoutFunc.length
  }

  /**
   * Used only for testing
   */
  private[this] def inactivityTimeout: FiniteDuration = inactivityTimeoutFunc

  override def add(session: Session, key: String): Boolean = map.synchronized {
    if (!map.keySet.contains(key)) {
      map += (key -> session)
      timeoutManager.handleTimeoutHandler(this, key, inactivityTimeout)
      true
    } else {
      false
    }
  }

  override def remove(key: String): Boolean =
    map.synchronized {
      println("cache remove called for key:" + key)
      if (map.keySet.contains(key)) {
        map.remove(key)
        timeoutManager.deleteTimeoutHandler(this, key)
        println("current cache size:" + this.numberOfSessions)
        true
      } else {
        false
      }
    }

  override def get(key: String): Option[Session] = {
    timeoutManager.handleTimeoutHandler(this, key, inactivityTimeout)
    map.get(key)
  }

  /**
   * Retrievs a user from the system without resetting the
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
  def f: FiniteDuration = {
    val value = Random.nextInt(20000) + 2000
    value milliseconds
  }
  val cache = new SessionMapStorage(new ActorBasedSessionTimeoutManager, f)
  (10 to 99).foreach { i =>
    val session = new SessionImpl
    session.id = "0000000key" + i
    cache.add(session, "0000000key" + i)
  }

  def main(args: Array[String]): Unit = {}

}