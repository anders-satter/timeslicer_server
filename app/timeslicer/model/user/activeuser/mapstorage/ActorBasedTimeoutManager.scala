package timeslicer.model.user.activeuser.mapstorage

import scala.concurrent.duration.FiniteDuration
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.ActorRef
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Cancellable
import timeslicer.model.user.activeuser.ActiveUserStorage
import timeslicer.model.user.activeuser.TimeoutManager


  case class AddRemoveCall(cache: ActiveUserStorage, key: String, duration: FiniteDuration)
  case class Deactivate()
  case class RemoveCall(key: String, cache: ActiveUserStorage)

  class TimedRemoverActor extends Actor with ActorLogging {
    private[this] var cancellable: Cancellable = null
    private[this] var _key = ""

    def receive = {
      case AddRemoveCall(cache, key, duration) => {
        log.info("Received AddRemoveCall for:" + key)
        _key = key
        import context.dispatcher
        /*
       * If we already have cancellable we cancel it
       * and assign a new one 
       */
        println("receiving key:" + key)
        if (cancellable != null) {
          println("cancellable already set for key:" + key)
          cancellable.cancel()
        }
        println("setting cancellable for key:" + key)
        cancellable = context.system.scheduler.scheduleOnce(duration, self, RemoveCall(key, cache))
      }
      case RemoveCall(key, cache) => {
        cache.remove(key);
      }

      case Deactivate() => {
        /*This this cancels the scheduler and stops this actor*/
        if (cancellable != null) {
          println("Stopping TimedRemoverHostActor with key:" + _key)
          cancellable.cancel()
        }
        context.stop(self)
      }
    }
  }

class ActorBasedTimeoutManager extends TimeoutManager{
  
  private[this]val actorSystem = ActorSystem()
  
  def handleTimeoutHandler(storage:ActiveUserStorage, key:String, timeout:FiniteDuration) = {
    handleRemover(actorSystem, storage, key, timeout)
  }
  def deleteTimeoutHandler(storage:ActiveUserStorage, key:String) = {
	  deleteRemover(actorSystem, storage, key)
  }


  private[this] val actorMap = new java.util.HashMap[String, ActorRef]

  def handleRemover(actorSystem: ActorSystem, cache: ActiveUserStorage, key: String, duration: FiniteDuration) = {

    if (actorMap.keySet().contains(key)) {
      println("actor found:" + key)
      var currentActor = actorMap.get(key)
      currentActor ! AddRemoveCall(cache, key, duration)
    } else {
      val remover = actorSystem.actorOf(Props(classOf[TimedRemoverActor]), key)
      remover ! AddRemoveCall(cache, key, duration)
      actorMap.put(key, remover)
    }
  }

  def deleteRemover(actorSystem: ActorSystem, cache: ActiveUserStorage, key: String) = {
    actorMap.synchronized {
      println("removing actorRef:" + key)
      actorMap.get(key) ! Deactivate
      actorMap.remove(key)
    }
  }

/*
 * Notes on actors  
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

  
}