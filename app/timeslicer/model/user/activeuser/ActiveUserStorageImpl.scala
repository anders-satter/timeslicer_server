package timeslicer.model.user.activeuser

import timeslicer.model.user.activeuser.mapstorage.ActiveUserMapStorage
import timeslicer.model.user.activeuser.mapstorage.ActorBasedTimeoutManager



/**
 * Will load the current active user storage implementation at
 * first load
 */
object ActiveUserStorageImpl{
  /**
   * add parenthesis to the apply method to be able to instantiate
   * this class with ActiveUserStorageImpl() (otherwise we need to 
   * say ActiveUserStorageImpl.apply)
   */
  def apply(): ActiveUserStorage = {
    new ActiveUserMapStorage(new ActorBasedTimeoutManager)
  }

}