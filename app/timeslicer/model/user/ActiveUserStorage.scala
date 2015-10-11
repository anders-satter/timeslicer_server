package timeslicer.model.user

trait ActiveUserStorage {
  /**
   * Adds user to the user storage with its key
   */
  def add(user:User, key:String):Boolean
  /**
   * Returns an option to the user stored with the
   * supplied key
   */
  def get(key:String):Option[User]
  def remove(key:String):Boolean
  def getByName(userName:String):Option[Seq[User]]
  def getById(id:String):Option[Seq[User]]
  def getByEmail(email:String):Option[Seq[User]]
}
