package timeslicer.model.context

import timeslicer.model.user.User
import timeslicer.model.user.UserImpl

class UseCaseContextImpl extends UseCaseContext {
  private var _user: User = null
  override def user:User = _user
  override def user_=(user: User): Unit = _user = user

  private var _sessionId: String = null
  override def sessionId:String = _sessionId
  override def sessionId_=(sId: String): Unit = _sessionId = sId

}