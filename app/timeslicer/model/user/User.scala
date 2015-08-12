package timeslicer.model.user

/**
 * User of the 
 */
trait User {
  def firstName:String
  def lastName:String
  def id:String
  def isAuthenticated:Boolean
  def isAuthorized:Boolean
  /**
   * Email is optional so...
   */
  def email:Option[String]
}