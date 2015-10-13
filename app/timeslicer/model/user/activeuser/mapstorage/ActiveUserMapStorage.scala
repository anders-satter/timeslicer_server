package timeslicer.model.user.activeuser.mapstorage

import scala.collection.mutable.HashMap

import timeslicer.model.user.activeuser.ActiveUserStorage
import timeslicer.model.user.activeuser.ActiveUser


class ActiveUserMapStorage extends ActiveUserStorage {  
  private[this] val map = new HashMap[String, ActiveUser]

  override def add(user: ActiveUser, key: String): Boolean =
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
      if (map.keySet.contains(key)) {
        map.remove(key)
        true
      } else  {
        false
      }
    }

  override def get(key:String):Option[ActiveUser] = {
    map.get(key)
  }
  override def getByName(userName: String): Option[Seq[ActiveUser]] = ???
  override def getById(id: String): Option[Seq[ActiveUser]] = ???
  override def getByEmail(email: String): Option[Seq[ActiveUser]] = ???
  override def numberOfUsers = map.size
  

}