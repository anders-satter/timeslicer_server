package timeslicer.model.storage.filestorage

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import timeslicer.model.context.UseCaseContext

@RunWith(classOf[JUnitRunner])
class FileStorageSpec extends Specification with Mockito {
  /*
   * SETUP
   */
  val useCaseContext = mock[UseCaseContext]
  val fileStorage = new FileStorage("data/prj.txt", "data/log.txt")
  
  
  

  /*
	 * TEST
	 */
  "FileStorage" should {
    "return project from prj.txt" in {
      //fileStorage.projects(useCaseContext) must beNull
      fileStorage.projects(useCaseContext).length > 0 must beTrue
    }
  }

}