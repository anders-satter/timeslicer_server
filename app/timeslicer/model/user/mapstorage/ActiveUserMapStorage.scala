package timeslicer.model.user.mapstorage

import scala.collection.mutable.HashMap

import timeslicer.model.user.ActiveUserStorage
import timeslicer.model.user.User

class ActiveUserMapStorage extends ActiveUserStorage {
  private[this] val map = new HashMap[String, User]

  override def add(user: User, key: String): Boolean =
    if (!map.keySet.contains(key)) {
      map.synchronized {
        map += (key -> user)
      }
      true
    } else {
      false
    }

  override def remove(key: String): Boolean =
    map.synchronized {
      if (!map.keySet.contains(key)) {
        map.remove(key)
        true
      } else  {
        false
      }
    }

  override def get(key:String):Option[User] = {
    map.get(key)
  }
  override def getByName(userName: String): Option[Seq[User]] = ???
  override def getById(id: String): Option[Seq[User]] = ???
  override def getByEmail(email: String): Option[Seq[User]] = ???

}