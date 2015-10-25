package timeslicer.model.user.activeuser.mapstorage

import scala.collection.mutable.HashMap
import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.FiniteDuration
import scala.util.Random
import timeslicer.model.user.activeuser.ActiveUser
import timeslicer.model.user.activeuser.ActiveUserStorage
import timeslicer.model.user.activeuser.TimeoutManager
import timeslicer.model.user.UserImpl
import timeslicer.model.user.activeuser.ActiveUserStorageProperties

/**
 * ActiveUserStorage implementation using a hashmap and
 * an Akka actor system for timeout management
 */
class ActiveUserMapStorage(val timeoutManager: TimeoutManager, inactivityTimeoutFunc: FiniteDuration)
  extends ActiveUserStorage {
  /**
   * Timeout of active users is taken care of
   * by an actor system
   */
  private[this] val map = new HashMap[String, ActiveUser]

  def properties: ActiveUserStorageProperties = new ActiveUserStorageProperties {
    def inactivityTimeoutDelay = inactivityTimeoutFunc.length
  }

  /**
   * Used only for testing
   */
  private[this] def inactivityTimeout: FiniteDuration = inactivityTimeoutFunc

  override def add(user: ActiveUser, key: String): Boolean =
    map.synchronized {
      if (!map.keySet.contains(key)) {
        map += (key -> user)
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
        println("current cache size:" + this.numberOfUsers)
        true
      } else {
        false
      }
    }

  override def get(key: String): Option[ActiveUser] = {
    timeoutManager.handleTimeoutHandler(this, key, inactivityTimeout)
    map.get(key)
  }

  /**
   * Retrievs a user from the system without resetting the
   * users timeout clock
   */
  override def systemGet(key: String): Option[ActiveUser] = map.get(key)

  override def getByName(userName: String): Option[Seq[ActiveUser]] = ???
  override def getById(id: String): Option[Seq[ActiveUser]] = ???
  override def getByEmail(email: String): Option[Seq[ActiveUser]] = ???
  override def numberOfUsers = map.size
  override def keySet: scala.collection.Set[String] = map.keySet
}

/**
 * Used for testing this
 */
object ActiveUserMapStorage {

  println("hello")

  def f: FiniteDuration = {    
    val value = Random.nextInt(20000) + 2000
    println(value)
    value milliseconds
  }

  val cache = new ActiveUserMapStorage(new ActorBasedTimeoutManager, f)
  (10 to 99).foreach { i =>
    val user = new UserImpl
    user.id = "0000000key" + i
    cache.add(ActiveUser(user), "0000000key" + i)
  }

  def main(args: Array[String]): Unit = {}
}

