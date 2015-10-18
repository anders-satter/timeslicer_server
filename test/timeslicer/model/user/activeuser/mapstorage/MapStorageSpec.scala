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
import akka.actor.ActorSystem
import scala.concurrent.duration.Duration
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._

import timeslicer.model.user.activeuser.ActiveUserStorage
import timeslicer.model.user.activeuser.TimeoutManager

@RunWith(classOf[JUnitRunner])
class MapStorageSpec extends Specification {

  /*
   * SETUP 
   */
  val mockedTimeoutManager = new TimeoutManager(){
      def handleTimeoutHandler(storage:ActiveUserStorage, key:String, timeout:FiniteDuration) = {}
      def deleteTimeoutHandler(storage:ActiveUserStorage, key:String) = {}

  }
  val mockedFunc = 0 milliseconds
  val storage = new ActiveUserMapStorage(mockedTimeoutManager, mockedFunc)  
  
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
    
    "verify existence of keys in storage" in {
      storage.keySet.contains(user1Key)
      storage.keySet.contains(user2Key)
      storage.keySet.contains(user3Key)
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