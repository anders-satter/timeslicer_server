package timeslicer.model.user

import timeslicer.model.user.mapstorage.ActiveUserMapStorage

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