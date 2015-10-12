package timeslicer.model.user.activeuser

import timeslicer.model.user.User

/**
 * Wrapper of a User with an added touch field
 */
case class ActiveUser(val user: User) {
  var _latestTouch: Long = 0L
  def latestTouch: Long = _latestTouch
  def latestTouch_(time: Long) = _latestTouch = time
}