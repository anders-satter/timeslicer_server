package timeslicer.model.user.activeuser

import timeslicer.model.user.User

/**
 * Wrapper of a User with an added touch field
 */
case class ActiveUser(val aUser: User) {

  private[this] var _latestTouch: Long = System.currentTimeMillis() //latest touch set att init
  def latestTouch = _latestTouch  
  
  private[this] var _user = aUser //user set at init
  
  /**
   * Each time user is retrieved latestTouch is reset
   */
  def user: User = {
    _latestTouch = System.currentTimeMillis()
    _user
  }
  /**
   * Retrieve user without resetting touch,
   * might be used for logging...
   */
  def userWithoutTouch: User = _user
}
