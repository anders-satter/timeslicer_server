package timeslicer.model.framework
import timeslicer.model.framework.impl._
import timeslicer.model.session.SessionStorage
trait InteractionIdManager {
  def interactionId(sessionStorageKey: String):String
}

/**
 * The returns the standard implementation of
 * the InteractionIdManager
 */
object InteractionIdManager{
  val implementation = new InteractionIdManagerImpl(SessionStorage())
  def apply():InteractionIdManager = {
    implementation
  }
}