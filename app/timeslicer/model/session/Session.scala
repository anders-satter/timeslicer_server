package timeslicer.model.session

import timeslicer.model.user.User

trait Session {
  def id: String
  def id_=(id:String):Unit
  
  def user: Option[User]
  def user_=(user:User):Unit
}