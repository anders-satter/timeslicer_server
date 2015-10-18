package timeslicer.model.user.activeuser.mapstorage

import scala.collection.mutable.HashMap
import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.FiniteDuration
import scala.util.Random

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Cancellable
import akka.actor.actorRef2Scala
import timeslicer.model.user.UserImpl
import timeslicer.model.user.activeuser.ActiveUser
import timeslicer.model.user.activeuser.ActiveUserStorage
import timeslicer.model.user.activeuser.TimeoutManager



/**
 * TimedRemover accepted messages
 */

/**
 * ActiveUserStorage implementation using a hashmap and
 * an Akka actor system for timeout management
 */
class ActiveUserMapStorage(val timeoutManager: TimeoutManager) extends ActiveUserStorage {
  /**
   * Timeout of active users is taken care of
   * by an actor system
   */
  
  private[this] val map = new HashMap[String, ActiveUser]

  /**
   * Used only for testing
   */
  private[this] def timeout: FiniteDuration = {
    val value = Random.nextInt(20000) + 2000
    value milliseconds
  }

  override def add(user: ActiveUser, key: String): Boolean =
    map.synchronized {
      if (!map.keySet.contains(key)) {
        map += (key -> user)
        timeoutManager.handleTimeoutHandler(this, key, timeout)
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
    timeoutManager.handleTimeoutHandler(this, key, timeout)
    map.get(key)
  }
  override def systemGet(key:String): Option[ActiveUser] = map.get(key)
  
  override def getByName(userName: String): Option[Seq[ActiveUser]] = ???
  override def getById(id: String): Option[Seq[ActiveUser]] = ???
  override def getByEmail(email: String): Option[Seq[ActiveUser]] = ???
  override def numberOfUsers = map.size
  override def keySet: scala.collection.Set[String] = map.keySet
  
}


object TimedRemoverFactory {
}

/**
 * Used for testing this
 * Actors are run for a timeslice, not in their own thread
 * Concurrency are lifted to workflow instead of using locks, wait, etc
 * start, stop, ! asynchronous message, reply
 * become redefine the behaviour state machines
 * unbecome HotSwap
 *
 * ?, the actor returns a future, will resolved in time
 * future.onComplete(f => ...)
 * reduce, fold,
 * etc
 *
 * Clustered actors
 *  Address
 */
//object ActiveUserMapStorage {
//  val cache = new ActiveUserMapStorage(new ActorBasedTimeoutManager)
//  (10 to 99).foreach { i =>
//    val user = new UserImpl
//    user.id = "0000000key" + i
//    cache.add(ActiveUser(user), "0000000key" + i)
//  }
//
//  def main(args: Array[String]): Unit = {}
//}

