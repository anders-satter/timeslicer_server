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

      //      println("--start the printout--------")
      //      fileStorage.projects(useCaseContext).get.foreach(p => {
      //        println(p.name)
      //        p.activityList.foreach(a => println(" " + a.name))
      //      })
      //      println("--end of printout--------")
      fileStorage.projects(useCaseContext) != None must beTrue
      fileStorage.projects(useCaseContext).get.length > 0 must beTrue
    }

    "return activities for project from prj.txt" in {
      //      println("--Team TDE--")
      //      fileStorage.activities(Project("Team TDE", null), useCaseContext).get.map(_.name) map println
      //      println("--jsr286--")
      //      fileStorage.activities(Project("jsr286", null), useCaseContext).get.map(_.name) map println
      //      println("--Miljö--")
      //      fileStorage.activities(Project("Miljö", null), useCaseContext).get.map(_.name) map println
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

      ok
    }
  }
}