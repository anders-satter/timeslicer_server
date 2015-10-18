package timeslicer.model.user.activeuser

import timeslicer.model.user.User

trait ActiveUserStorage {
  
  /**
   * Adds user to the user storage with its key
   */
  def add(user: ActiveUser, key: String): Boolean
  
  /**
   * Returns an option to the user stored with the
   * supplied key
   */
  def get(key: String): Option[ActiveUser]

  /**
   * Returns an option to the user stored with the
   * supplied key
   */  
  def remove(key: String): Boolean
  def getByName(userName: String): Option[Seq[ActiveUser]]
  def getById(id: String): Option[Seq[ActiveUser]]
  def getByEmail(email: String): Option[Seq[ActiveUser]]
  def numberOfUsers:Long
  def keySet:scala.collection.Set[String]
  /**
   * returns the user without 'touching' her, 
   * so timeout mechanism should not be reset.
   * This getter should only be used for monitoring
   * purposes
   */
  def systemGet(key:String): Option[ActiveUser]
}
