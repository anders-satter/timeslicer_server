package timeslicer.model.session.mapstorage

import timeslicer.model.session.SessionTimeoutManager
import timeslicer.model.session.SessionStorage
import scala.concurrent.duration.FiniteDuration
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Cancellable
import akka.actor.ActorSystem
import akka.actor.ActorRef
import akka.actor.Props

case class AddRemoveMesssage(cache: SessionStorage, key: String, duration: FiniteDuration)
case class DeactivateMessage()
case class DoRemoveCall(key: String, cache: SessionStorage)

class TimedSessionRemovalActor extends Actor with ActorLogging {
  private[this] var cancellable: Cancellable = null
  private[this] var _key = ""

  def receive = {
    case AddRemoveMesssage(cache, key, duration) => {
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
      cancellable = context.system.scheduler.scheduleOnce(duration, self, DoRemoveCall(key, cache))
    }
    case DoRemoveCall(key, cache) => {
      cache.remove(key);
    }

    case DeactivateMessage() => {
      /*This this cancels the scheduler and stops this actor*/
      if (cancellable != null) {
        println("Stopping TimedRemoverHostActor with key:" + _key)
        cancellable.cancel()
      }
      context.stop(self)
    }
  }
}

class ActorBasedSessionTimeoutManager extends SessionTimeoutManager {
  private[this] val actorSystem = ActorSystem()
  private[this] val actorMap = new java.util.HashMap[String, ActorRef]

  
  
  def handleTimeoutHandler(storage:SessionStorage, key:String, timeout:FiniteDuration) = {
    handleRemover(actorSystem, storage, key, timeout)
  }
  def deleteTimeoutHandler(storage:SessionStorage, key:String) = {
    deleteRemover(actorSystem, storage, key)
  }

  
  
  def handleRemover(actorSystem: ActorSystem, cache: SessionStorage, key: String, duration: FiniteDuration) = {

    if (actorMap.keySet().contains(key)) {
      println("actor found:" + key)
      var currentActor = actorMap.get(key)
      currentActor ! AddRemoveMesssage(cache, key, duration)
    } else {
      val remover = actorSystem.actorOf(Props(classOf[TimedSessionRemovalActor]), key)
      remover ! AddRemoveMesssage(cache, key, duration)
      actorMap.put(key, remover)
    }
  }

  def deleteRemover(actorSystem: ActorSystem, cache: SessionStorage, key: String) = {
    actorMap.synchronized {
      println("removing actorRef:" + key)
      actorMap.get(key) ! DeactivateMessage
      actorMap.remove(key)
    }
  }
}

