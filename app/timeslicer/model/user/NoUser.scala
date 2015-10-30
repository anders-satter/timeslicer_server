package timeslicer.model.user

object NoUser extends User {
  override def userName = "NoUser"
  override def firstName = "NoUser"
  override def lastName = "NoUser"
  override def id = "***NOUSER***"
  override def email = None
  override def isAuthenticated = false
  override def isAuthenticated_=(value: Boolean):Unit = {}
  
  override def isAuthorized = false
  override def isAuthorized_=(value: Boolean):Unit = {}
  override def validate = true
  override def passwordHash = ""
  override def passwordSalt = ""  
}