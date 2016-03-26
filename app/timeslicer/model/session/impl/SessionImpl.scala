package timeslicer.model.session.impl

import timeslicer.model.session.Session
import timeslicer.model.user.User
import timeslicer.model.user.NoUser

class SessionImpl extends Session {
  /**
   * The user is initiated to a NoUser
   */
  private [this] var _user:User = NoUser
  /**
   * user always returns a user, be it a NoUser
   */
  override def user:User = _user 
  override def user_=(user:User):Unit = _user = user
  
  private [this] var _id:String = ""
  override def id: String = _id 
  def id_=(id:String):Unit = _id = id
}