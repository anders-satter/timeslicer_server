package timeslicer.model.user

import timeslicer.model.user.mapstorage.ActiveUserMapStorage

/**
 * Holds ActiveUserStorage and should be used to add
 * and remove users from it.
 */
object UserManagerImpl extends UserManager {
  private[this] val _activeUserStorage:ActiveUserStorage = ActiveUserStorageImpl()
  def activeUserStorage:ActiveUserStorage = _activeUserStorage
}