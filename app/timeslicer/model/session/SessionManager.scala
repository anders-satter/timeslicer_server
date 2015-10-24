package timeslicer.model.session

/**
 * Initializes the session storage
 * returns the current session
 * retrieves the user from storage
 * and puts it in the current session
 */
trait SessionManager {
  /**
   * current session
   */
  def session:Session
  
}