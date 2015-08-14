package timeslicer.model.storage.filestorage

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import timeslicer.model.context.UseCaseContext
import timeslicer.model.project.Project
import timeslicer.model.util.FileCommunicationUtil
import timeslicer.model.util.settings.Settings
import timeslicer.model.project.Project
import timeslicer.model.project.Activity
import timeslicer.model.project.Activity
import play.Play
import timeslicer.model.user.User
import timeslicer.model.context.UseCaseContext

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
  //println(fileStorage.calcProjectFileName(useCaseContext))
  val projectFileSeq = FileCommunicationUtil.readFromFileToStringArray(fileStorage.calcProjectFileName(useCaseContext), Settings.propertiesMap("ProjectFileEncoding")).toSeq
  //println("strSeq: " + strSeq.length)
  /*
	 * TEST
	 */
  "FileStorage" should {
    "return projects from prj.txt" in {
      if (projectFileSeq.length > 0) {
        fileStorage.projects(useCaseContext) != None must beTrue
        fileStorage.projects(useCaseContext).get.length > 0 must beTrue
      } else {
        ok
      }
    }

    """should assert that number of projects returned from
      projects service should be the same as all #<project-name> items
      in the prj.txt""" in {
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

    """assert that the sum of all the projects 
     and activities equals all items in in the file""" in {
      val strSeq = FileCommunicationUtil.readFromFileToStringArray(
        fileStorage.calcProjectFileName(
          useCaseContext), Settings.propertiesMap("ProjectFileEncoding")).toSeq
      val ret = fileStorage.projects(useCaseContext)
      if (strSeq.length < 1 && ret == None) {
        ok
      } else {
        val itemsInFileCount = strSeq.length
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

    "return all TimeSlice's in an interval" in {
      val strSeq = FileCommunicationUtil.readFromFileToStringArray(
        fileStorage.calcLogFileName(
          useCaseContext), Settings.propertiesMap("LogFileEncoding")).toSeq
      val ret = fileStorage.timeslices("2014-04-13", "2015-01-07", useCaseContext)
      if (strSeq.length < 1 && ret == None ){
        ok
      } else {
        //fileStorage.timeslices("2014-04-13","2015-01-07", useCaseContext).get foreach println
        fileStorage.timeslices("2014-04-13", "2015-01-07", useCaseContext) != None must beTrue
        fileStorage.timeslices("2014-04-13", "2015-01-07", useCaseContext).get.length > 0 must beTrue
      }
    }

    "return users in users.json" in {
      //      fileStorage.users.get.foreach(item => {
      //        println(item.toString)
      //        println("-------------")
      //      })

      fileStorage.users != None must beTrue
    }

    "add project" in {
      ok
    }

    "add activity" in {
      fileStorage.addActivity(Project("webtrends", null), Activity("newAct1"), useCaseContext)
      ok
    }

    "file location test" in {
      //println(Play.application().path())
      ok
    }

  }
}