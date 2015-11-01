package timeslicer.model.session

import timeslicer.model.user.User

trait Session {
  def id: String
  def id_=(id:String):Unit
  
  /**
   * session must always return a user, 
   * be it a NoUser
   */
  def user: User
  def user_=(user:User):Unit
}