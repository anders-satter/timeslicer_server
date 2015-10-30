package timeslicer.model.user

object SystemUser extends User{
  override def userName = "System"
  override def firstName = "System" 
  override def lastName = "System"
  override def id = "***SYSTEM***"
  override def email = None  
  override def isAuthenticated = true
  override def isAuthenticated_=(value: Boolean):Unit = {}
  override def isAuthorized = true   
  override def isAuthorized_=(value: Boolean):Unit = {}
  override def validate = true   
  override def passwordHash = ""   
  override def passwordSalt = ""   
  
}