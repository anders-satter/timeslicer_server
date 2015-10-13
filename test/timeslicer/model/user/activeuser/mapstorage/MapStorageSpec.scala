package timeslicer.model.user.activeuser.mapstorage

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import timeslicer.test.util.TestUtil
import org.specs2.runner.JUnitRunner
import timeslicer.model.user.activeuser.ActiveUser
import timeslicer.model.user.UserImpl
import timeslicer.model.util.StringIdGenerator
import timeslicer.model.user.User
import timeslicer.model.user.NoUser

@RunWith(classOf[JUnitRunner])
class MapStorageSpec extends Specification {

  /*
   * SETUP 
   */
  val storage = new ActiveUserMapStorage
  val user1Key = StringIdGenerator.activeUserStorageKey()
  val user2Key = StringIdGenerator.activeUserStorageKey()
  val user3Key = StringIdGenerator.activeUserStorageKey()

  "ActiveUserMapStorageTest" should {
    "add an ActiveUser to map storage" in {
      val user1 = new UserImpl
      user1.userName = "first"
      user1.id = "111111111111"
      val user2 = new UserImpl
      user2.userName = "second"
      user2.id = "222222222222"
      val user3 = new UserImpl
      user3.userName = "third"
      user3.id = "333333333333"

      val activeUser = ActiveUser(TestUtil.testUser)

      //      println(user1Key)
      //      println(user2Key)
      //      println(user3Key)

      storage.add(ActiveUser(user1), user1Key)
      storage.add(ActiveUser(user2), user2Key)
      storage.add(ActiveUser(user3), user3Key)

      storage.numberOfUsers == 3
    }
    "retrieve users from storage" in {
      storage.get(user1Key).getOrElse(ActiveUser(NoUser)).user.id == "111111111111"
      storage.get(user2Key).getOrElse(ActiveUser(NoUser)).user.id == "222222222222"
      storage.get(user3Key).getOrElse(ActiveUser(NoUser)).user.id == "333333333333"
    }
    "remove an ActiveUser from map storage" in {
      storage.numberOfUsers == 3
      storage.remove(user1Key)
      storage.numberOfUsers == 2
      storage.remove(user2Key)
      storage.numberOfUsers == 1      
      storage.remove(user3Key)
      storage.numberOfUsers == 0
      
    }
    "remove an obsolete ActiveUser from map storage" in {
      pending
    }
  }
}