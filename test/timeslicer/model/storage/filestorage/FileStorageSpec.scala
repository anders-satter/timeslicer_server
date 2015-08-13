package timeslicer.model.storage.filestorage

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import timeslicer.model.context.UseCaseContext
import java.io.File
import timeslicer.model.project.Project
import timeslicer.model.util.FileCommunicationUtil
import timeslicer.model.util.settings.Settings

@RunWith(classOf[JUnitRunner])
class FileStorageSpec extends Specification with Mockito {
  /*
   * SETUP
   */
  val useCaseContext = mock[UseCaseContext]
  val fileStorage = new FileStorage("test/data/prj.txt", "test/data/log.txt", "test/data/users.txt")

  /*
	 * TEST
	 */
  "FileStorage" should {
    "return projects from prj.txt" in {
      fileStorage.projects(useCaseContext) != None must beTrue
      fileStorage.projects(useCaseContext).get.length > 0 must beTrue
    }

    """should assert that number of projects returned from
      projects service should be the same as all #<project-name> items
      in the prj.txt""" in {
      /*
       * count #<project-name> items in prj.txt
       */
      val strSeq = FileCommunicationUtil.readFromFile("test/data/prj.txt", Settings.propertiesMap("ProjectFileEncoding")).toSeq
      var count = 0
      strSeq.foreach(item => {
        if (item.startsWith("#")) {
          count = count + 1
        }
      })
      count == fileStorage.projects(useCaseContext).get.length must beTrue

    }

    "return activities for project from prj.txt" in {
      fileStorage.activities(Project("Team TDE", null), useCaseContext) != None must beTrue
    }

    """assert that the sum of all the projects 
     and activities equals all items in in the file
    """ in {
      val strSeq = FileCommunicationUtil.readFromFile("test/data/prj.txt", Settings.propertiesMap("ProjectFileEncoding")).toSeq
      val itemsInFileCount = strSeq.length
      //get all the projects in prj.txt
      val projects = fileStorage.projects(useCaseContext).get
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
          case None => {}
        }
      })
      itemsInFileCount == count must beTrue
    }

    "return all TimeSlice's in an interval" in {
    	println(fileStorage.timeslices("2014-04-13","2015-01-07", useCaseContext))      
      fileStorage.timeslices("2014-04-13","2015-01-07", useCaseContext) != None must beTrue      
    }

  }
}