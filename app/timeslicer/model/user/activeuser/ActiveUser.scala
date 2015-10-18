package timeslicer.model.user.activeuser

import timeslicer.model.user.User

/**
 * Wrapper of a User with an added touch field
 * Not sure how this touch field will be used yet
 * the inactivityTimout management is done on
 * ActiveUserStorage level, and is not affected by
 * retrieving the user from this object
 */
case class ActiveUser(val aUser: User) {

  private[this] var _latestTouch: Long = System.currentTimeMillis() //latest touch set att init
  def latestTouch = _latestTouch  
  
  /**
   * Each time user is retrieved latestTouch is reset
   */
  def user: User = {
    _latestTouch = System.currentTimeMillis()
    aUser
  }

  /**
   * Retrieve user without resetting touch,
   * might be used for logging or other 
   * system related activities that should not
   * affect the users ...
   */
  def userWithoutTouch: User = aUser
}
