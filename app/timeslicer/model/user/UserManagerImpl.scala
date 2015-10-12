package timeslicer.model.user

import timeslicer.model.user.activeuser.mapstorage.ActiveUserMapStorage
import timeslicer.model.user.activeuser.ActiveUserStorage
import timeslicer.model.user.activeuser.ActiveUserStorageImpl

/**
 * Holds ActiveUserStorage and should be used to add
 * and remove users from it.
 */
object UserManagerImpl extends UserManager {
  private[this] val _activeUserStorage:ActiveUserStorage = ActiveUserStorageImpl()
  def activeUserStorage:ActiveUserStorage = _activeUserStorage
}