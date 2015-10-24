package timeslicer.model.session.impl

import timeslicer.model.session.Session
import timeslicer.model.user.User
import timeslicer.model.user.NoUser

class SessionImpl extends Session {
  private [this] var _user:User = NoUser
  override def user: User = _user 
  def user_=(user:User):Unit = _user = user
  
  private [this] var _id:String = ""
  override def id: String = _id 
  def id_=(id:String):Unit = _id = id
}