package timeslicer.model.user

import timeslicer.model.user.activeuser.ActiveUserStorage

trait UserManager {
  def activeUserStorage:ActiveUserStorage 
}