package timeslicer.model.user.activeuser

import timeslicer.model.user.activeuser.mapstorage.ActiveUserMapStorage


/**
 * Will load the current active user storage implementation at
 * first load
 */
object ActiveUserStorageImpl{
  val currentActiveUsersImplementation = new ActiveUserMapStorage
  def apply(): ActiveUserStorage = {
    return currentActiveUsersImplementation
  }

}