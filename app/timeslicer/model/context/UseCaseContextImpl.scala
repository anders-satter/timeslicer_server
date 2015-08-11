package timeslicer.model.context

import timeslicer.model.user.User
import timeslicer.model.user.UserImpl

class UseCaseContextImpl extends UseCaseContext {
  private var _user: User = null

  def user = _user
  def user_= (user: User): Unit = _user = user
 
}