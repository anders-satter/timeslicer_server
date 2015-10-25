package timeslicer.model.session

/**
 * Initializes the session storage
 * returns the current session
 * retrieves the user from storage
 * and puts it in the current session
 */
trait SessionManager {
  /**
   * current session, if the session is not new there should be a user
   * connected to the session
   */
  def session(key: String): Session
  def sessionExists(key:String):Boolean

}