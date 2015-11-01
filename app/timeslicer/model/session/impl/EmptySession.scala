package timeslicer.model.session.impl

import timeslicer.model.session.Session
import timeslicer.model.user.NoUser
import timeslicer.model.user.User

/**
 * Returns an empty session with NoUser
 */
object EmptySession extends Session {
  override def id: String = "EMPTY_SESSION"
  override def id_=(id: String): Unit = {}
  
  override def user:User = NoUser
  override def user_=(user:User):Unit = {}
}