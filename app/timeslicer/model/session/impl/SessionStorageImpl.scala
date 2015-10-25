package timeslicer.model.session.impl

import timeslicer.model.session.SessionStorage
import timeslicer.model.session.mapstorage.SessionMapStorage
import timeslicer.model.session.mapstorage.ActorBasedSessionTimeoutManager
/**
 * Loads the current session storage implementation
 */
object SessionStorageImpl {
  def apply(): SessionStorage = {
    new SessionMapStorage(new ActorBasedSessionTimeoutManager, new SessionStoragePropertiesImpl)
  }

}