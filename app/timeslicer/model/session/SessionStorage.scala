package timeslicer.model.session

trait SessionStorage {
  
    /**
   * Adds user to the user storage with its key
   */
  def add(session: Session, key: String): Boolean
  
  /**
   * Returns an option to the user stored with the
   * supplied key
   */
  def get(key: String): Option[Session]

  /**
   * Returns an option to the user stored with the
   * supplied key
   */  
  def remove(key: String): Boolean
  def numberOfSessions:Long
  def keySet:scala.collection.Set[String]
  /**
   * returns the session without 'touching' it, 
   * so timeout mechanism should not be reset.
   * This getter should only be used for monitoring
   * and internal purposes
   */
  def systemGet(key:String): Option[Session]
  
  def properties:SessionStorageProperties


}