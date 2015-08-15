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
import timeslicer.model.project.Project
import timeslicer.model.project.Project

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
  val projectFileSeq = FileCommunicationUtil.readFromFileToStringArray(fileStorage.calcProjectFileName(useCaseContext), Settings.propertiesMap("ProjectFileEncoding")).toSeq.filter(i => i.length() > 1)
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

    "return all TimeSlice's in an interval" in {
      val strSeq = FileCommunicationUtil.readFromFileToStringArray(
        fileStorage.calcLogFileName(
          useCaseContext), Settings.propertiesMap("LogFileEncoding")).toSeq
      val ret = fileStorage.timeslices("2014-04-13", "2015-01-07", useCaseContext)
      if (strSeq.length < 1 && ret == None) {
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
      println(fileStorage.projects(useCaseContext).get)
      (fileStorage.projects(useCaseContext).get.filter(p => p.name == project1.name).length > 0) must beTrue
      (fileStorage.projects(useCaseContext).get.filter(p => p.name == project2.name).length > 0) must beTrue
      fileStorage.projects(useCaseContext).get.length == originalLength + 2 must beTrue

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