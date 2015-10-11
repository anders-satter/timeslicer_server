package timeslicer.model.user

object SystemUser extends User{
  override def firstName = "System" 
  override def lastName = "System"
  override def id = "***SYSTEM***"
  override def email = None  
  override def isAuthenticated = true
  override def isAuthorized = true   
  override def validate = true   
  override def latestTouch = 0L
  
}