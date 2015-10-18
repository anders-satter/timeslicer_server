package timeslicer.model.user

import timeslicer.model.user.activeuser.mapstorage.ActiveUserMapStorage
import timeslicer.model.user.activeuser.ActiveUserStorage
import timeslicer.model.user.activeuser.ActiveUserStorageImpl
import scala.concurrent.duration._
import timeslicer.model.util.settings.Settings

/**
 * Holds ActiveUserStorage and should be used to add
 * and remove users from it.
 */
object UserManagerImpl extends UserManager {
  private val inactivityTimeoutDelay:String = Settings.activeUser_inactivityTimeoutDelay
  private def inactiveTimeout: FiniteDuration = Integer.valueOf(15 * 60 * 1000).intValue() milliseconds
  /**
   * this should be read from settings.properties
   */
     
  private[this] val _activeUserStorage: ActiveUserStorage = ActiveUserStorageImpl(inactiveTimeout)
  def activeUserStorage: ActiveUserStorage = _activeUserStorage
}