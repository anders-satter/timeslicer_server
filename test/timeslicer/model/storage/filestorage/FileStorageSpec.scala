package timeslicer.model.storage.filestorage

import scala.collection.mutable.ListBuffer
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.libs.json.JsLookupResult.jsLookupResultToJsLookup
import play.api.libs.json.JsString
import play.api.libs.json.JsValue.jsValueToJsLookup
import play.api.libs.json.Json
import timeslicer.model.context.UseCaseContext
import timeslicer.model.message.MessageBuilder
import timeslicer.model.project.Activity
import timeslicer.model.project.Project
import timeslicer.model.storage.exception.ItemAlreadyExistsException
import timeslicer.model.timeslice.TimeSlice
import timeslicer.model.user.User
import timeslicer.model.user.UserImpl
import timeslicer.model.util.FileCommunicationUtil
import timeslicer.model.util.settings.Settings
import org.specs2.runner.JUnitRunner
import timeslicer.model.usecase.userid.CreateUserIdInteractor
import timeslicer.model.usecase.userid.CreateUserIdRequestModel
import timeslicer.model.storage.StorageFailResult
import timeslicer.model.util.Util
import timeslicer.test.util.TestUtil

@RunWith(classOf[JUnitRunner])
class FileStorageSpec extends Specification with Mockito {
  /*
   * SETUP
   */
  val useCaseContext = mock[UseCaseContext]
  val mockedUser = mock[User]
  mockedUser.id returns "222222222222"
  useCaseContext.user returns mockedUser

  val testFileStorageBasePath = "test/filestorage/data"
  val testPrjFileName = "prj.txt"
  val testLogFileName = "log.txt"
  val testUsersFileName = "users.json"
  val fileStorage = new FileStorage(testFileStorageBasePath, testPrjFileName, testLogFileName, testUsersFileName)

  /*
   * Load contents of the log.txt file
   */
  val projectFileSeq = FileCommunicationUtil
    .readFromFileToStringArray(fileStorage
      .calcProjectFileName(useCaseContext), Settings.propertiesMap("ProjectFileEncoding"))
    /*
       * this is to avoid lines with only a '\n' character 
       */
    .toSeq.filter(i => i.length() > 1)

  //println("strSeq: " + strSeq.length)
  /*
	 * TEST
	 */
  "Project test" should {
    //    "return projects from prj.txt" in {
    //      if (projectFileSeq.length > 0) {
    //        fileStorage.projects(useCaseContext) != None must beTrue
    //        fileStorage.projects(useCaseContext).get.length > 0 must beTrue
    //      } else {
    //        ok
    //      }
    //    }

    /*
     * Asserts that the number of projects in in prj.txt
     * are the same as returned from FileStorage.projects
     */
    def assertProjects = {
      /*
       * count #<project-name> items in prj.txt
       */
      val strSeq = FileCommunicationUtil.readFromFileToStringArray(fileStorage.calcProjectFileName(useCaseContext), Settings.propertiesMap("ProjectFileEncoding")).toSeq
      var count = 0
      strSeq.foreach(item => {
        if (item.startsWith("#")) {
          count = count + 1
        }
      })
      if (fileStorage.projects(useCaseContext) != None) {
        count == fileStorage.projects(useCaseContext).get.length must beTrue
      } else {
        ok
      }
    }

    def assertProjectsAndActivities = {
      val strSeq = FileCommunicationUtil.readFromFileToStringArray(
        fileStorage.calcProjectFileName(
          useCaseContext), Settings.propertiesMap("ProjectFileEncoding")).toSeq.filter(i => i.length() > 1)
      val ret = fileStorage.projects(useCaseContext)
      if (strSeq.length < 1 && ret == None) {
        ok
      } else {
        val itemsInFileCount = strSeq.filter(i => i.length() > 1).length
        val projects = ret.get
        //get all the projects in prj.txt
        /*
        * foreach project we are going to call the activities method
        * to get all the activity items for that project
        */
        var count = 0
        projects.foreach(p => {
          count = count + 1
          fileStorage.activities(p, useCaseContext) match {
            case Some(list) => {
              list.foreach(a => {
                count = count + 1
              })
            }
            case None => /*do nothing*/
          }
        })
        itemsInFileCount == count must beTrue
      }

    }

    """should assert that number of projects returned from
      projects service should be the same as all #<project-name> items
      in the prj.txt""" in {
      assertProjects
    }

    """assert that the sum of all the projects 
     and activities equals all items in in the file""" in {
      assertProjectsAndActivities
    }
    "remove project in FileStorageUtil" in {
      val projectSeq: Seq[Project] = Seq(Project("prj1", null), Project("prj2", null), Project("prj3", null))
      //println(projectSeq)
      val removedPrj2 = FileStorageUtil.performProjectRemoval(Project("prj2", null), projectSeq)
      //println(removedPrj2)
      ok
    }

    "perform project CRD in FileStorage" in {
      val project1 = Project("test1", null)
      val project2 = Project("test2", null)

      /*
       * First remove the projects to assure that
       * we begin from scratch
       * if neither of the exists, nothing will 
       * happen
       */
      fileStorage.removeProject(project1, useCaseContext)
      fileStorage.removeProject(project2, useCaseContext)

      /*
       * Check the number of persistent objects to begin with
       */
      val originalLength = {
        fileStorage.projects(useCaseContext) match {
          case Some(l) => {
            l.length
          }
          case None => {
            0
          }
        }
      }

      fileStorage.projects(useCaseContext) match {
        case Some(l) => {
          (l.filter(p => p.name == project1.name).length < 1) must beTrue
          (l.filter(p => p.name == project2.name).length < 1) must beTrue
        }
        case None => {
          ok
        }
      }

      /*
       * Add the project and assert it is there
       */
      fileStorage.addProject(project1, useCaseContext)
      fileStorage.addProject(project2, useCaseContext)
      //println(fileStorage.projects(useCaseContext).get)
      (fileStorage.projects(useCaseContext).get.filter(p => p.name == project1.name).length > 0) must beTrue
      (fileStorage.projects(useCaseContext).get.filter(p => p.name == project2.name).length > 0) must beTrue
      fileStorage.projects(useCaseContext).get.length == originalLength + 2 must beTrue
      assertProjectsAndActivities

      /*
       * Remove the project again 
       */
      fileStorage.removeProject(project1, useCaseContext)
      fileStorage.projects(useCaseContext) match {
        case Some(l) => {
          (l.filter(p => p.name == project1.name).length < 1) must beTrue
        }
        case None => {
          ok
        }
      }
      fileStorage.removeProject(project2, useCaseContext)
      fileStorage.projects(useCaseContext) match {
        case Some(l) => {
          (l.filter(p => p.name == project1.name).length < 1) must beTrue
        }
        case None => {
          ok
        }
      }
    }
  }

  "Activity test" should {
    /*
       * define a project
       */
    val project1 = Project("project1", null)
    /*
    	 * first remove the project to be sure
       * that we can add it...
    	 */
    fileStorage.removeProject(project1, useCaseContext)
    /*
       * ...then add it again
       */
    fileStorage.addProject(Project("project1", null), useCaseContext)
    /*
       * add the activity
       */
    fileStorage.addActivity(Project("project1", null), Activity("newAct1"), useCaseContext);

    "Verify that the activity was added" in {
      ok
    }

    "Return a StorageFailResult" in {
      fileStorage.addActivity(Project("project1", null), Activity("newAct1"), useCaseContext) must 
        be equalTo(Left(StorageFailResult("activity name already exists in the activity list for the project")))
      ok
    }

    "Remove the activity" in {
      fileStorage.removeActivity(Project("project1", null), Activity("newAct1"), useCaseContext);
      fileStorage.projects(useCaseContext) match {
        case Some(prjList) => {
          
          /* This is were we should be at this point, since we should have project1 still defined */
          prjList.filter(p => p.name == project1.name).map(p => {
            p.activityList.indexOf(Activity("newAct1")) == -1 must beTrue
          })
        }
        case None => {
          ok
        }
      }
      ok
    }

    "Remove the project" in {
      fileStorage.removeProject(project1, useCaseContext)
      fileStorage.projects(useCaseContext) match {
        case Some(pl) => {
          pl.filter(p => p.name == project1.name).length < 1 must beTrue
        }
        case None => ok
      }

      /* Another removal of a project should not lead to anything */
      fileStorage.removeProject(project1, useCaseContext)
      ok
    }
  }

  "TimeSlice test" should {
    /*
     * create a timeslice
     */
    val timeslice1 = TimeSlice("2015-08-14", "10:30", "2015-08-14","10:55", "TProject", "TActivity", Option("comment"))

    "add a TimeSlice" in {
      fileStorage.addTimeSlice(timeslice1, useCaseContext)
      fileStorage.timeslices(timeslice1.startdate, timeslice1.enddate, useCaseContext) match {
        case Some(tlist) => {
          tlist.length > 0 must beTrue
        }
        case None => {
          failure
        }
      }
      ok
    }
    "return all TimeSlices in an interval" in {
      val strSeq = FileCommunicationUtil.readFromFileToStringArray(
        fileStorage.calcLogFileName(
          useCaseContext), Settings.propertiesMap("LogFileEncoding")).toSeq
      val ret = fileStorage.timeslices(timeslice1.startdate, timeslice1.enddate, useCaseContext)
      if (strSeq.length < 1 && ret == None) {
        ok
      } else {
        //fileStorage.timeslices("2014-04-13","2015-01-07", useCaseContext).get foreach println
        fileStorage.timeslices(timeslice1.startdate, timeslice1.enddate, useCaseContext) != None must beTrue
        fileStorage.timeslices(timeslice1.startdate, timeslice1.enddate, useCaseContext).get.length > 0 must beTrue
      }
    }

    "remove a TimeSlice... - no functionality for removing a TimeSlice yet" in {
      ok
    }
  }
  "Users test" should {
    /*
     * should assert that all users in user.json 
     * also are shown in the FileStorage.users service
     */
    def assertFileToServiceConsistency = {
      /*read the user from the file*/
      val fileContent = FileCommunicationUtil
        .readFromFileToString(fileStorage.calcUsersFileName(), Settings.propertiesMap("ProjectFileEncoding"))
      val usersInFileCount = {
        if (fileContent.trim().length() < 1) {
          0
        } else {
          val json = Json.parse(fileContent)
          val userids = (json \ "users" \\ "id").asInstanceOf[ListBuffer[JsString]]
          userids.length
        }
      }

      //Representational State Transfer Protocol
      //post request then we need to put the
      //data in the body of the request
      /*call the FileStorage.users service*/
      val usersInServiceCount = fileStorage.users() match {
        case Some(seq) => seq.length
        case None      => 0
      }
      usersInFileCount == usersInServiceCount must beTrue
    }
    "Assert that all user in user.json are returned in the FileStorage.users" in {
      assertFileToServiceConsistency
    }

    "test user matches" should {

      /*
       * SETUP
       */
      val user1 = new UserImpl
      user1.firstName = "First"
      user1.lastName = "Sirname"
      user1.id = "111111111111"
      user1.isAuthenticated = false
      user1.isAuthorized = false
      user1.email = "" // empty email

      val user2 = new UserImpl
      user2.firstName = "Second"
      user2.lastName = "Sirname"
      user2.id = "222222222222"
      user2.isAuthenticated = false
      user2.isAuthorized = false
      user2.email = "abc2@example.com"

      val user3 = new UserImpl
      user3.firstName = "Third"
      user3.lastName = "Sirname"
      user3.id = "333333333333"
      user3.isAuthenticated = false
      user3.isAuthorized = false
      user3.email = "abc3@example.com"

      val users = Seq(user1, user2, user3)

      val user4 = new UserImpl
      user4.firstName = "Fourth"
      user4.lastName = "Sirname"
      user4.id = "444444444444"
      user4.isAuthenticated = false
      user4.isAuthorized = false
      user4.email = "abc4@example.com"

      /*
       * TESTS  
       */
      "user email should match" in {
        Util.matchesEmail(users, user3) must beTrue
      }
      "user email should not match" in {
        Util.matchesEmail(users, user4) must beFalse
      }

      "user name should match" in {
        Util.matchesUserName(users, user3) must beTrue
      }
      "user name should not match" in {
        Util.matchesUserName(users, user4) must beFalse
      }

      "user id should match" in {
        FileStorageUtil.matchesId(users, user3) must beTrue
      }
      "user id should not match" in {
        FileStorageUtil.matchesId(users, user4) must beFalse
      }
    }

    val userIdInteractor = new CreateUserIdInteractor
    userIdInteractor.log_=(TestUtil.emptyLog)
    val testUserid = userIdInteractor.execute(new CreateUserIdRequestModel).success.map(u => u.userId).getOrElse("")
    val testUser = new UserImpl
    testUser.firstName = "TestFörnamn"
    testUser.lastName = "TestEfternamn"
    testUser.id = testUserid
    testUser.isAuthenticated = false
    testUser.isAuthorized = false
    testUser.email = "abc@example.com"

    "add a new user" in {
      val userIdInteractor = new CreateUserIdInteractor
      fileStorage.addUser(testUser)
      ok
    }
    "remove a user" in {
      //fileStorage.users() map println

      //val testUser = new UserImpl
      testUser.firstName = "TestFörnamn"
      testUser.lastName = "TestEfternamn"
      testUser.id = testUserid
      testUser.isAuthenticated = false
      testUser.isAuthorized = false
      testUser.email = "abc@example.com"

      fileStorage.removeUser(testUser)
      ok
    }

  }

  //  "Location test" should {
  //	  "return application file location" in {
  //		  //println(Play.application().path())
  //		  ok
  //	  }
  //  }
}