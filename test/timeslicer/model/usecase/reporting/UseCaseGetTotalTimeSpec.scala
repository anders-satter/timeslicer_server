package timeslicer.model.usecase.reporting

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import timeslicer.model.user.User
import timeslicer.model.context.UseCaseContext
import timeslicer.model.storage.filestorage.FileStorage
import timeslicer.test.util.TestUtil
import timeslicer.model.util.{ Util => u, DateTime => dt }
import timeslicer.model.storage.Storage

@RunWith(classOf[JUnitRunner])
class UseCaseGetTotalTimeSpec extends Specification with Mockito {
  /*
   * SETUP
   */

  //  val testFileStorageBasePath = "test/filestorage/data"
  //  val testPrjFileName = "prj.txt"
  //  val testLogFileName = "log.txt"
  //  val testUsersFileName = "users.json"
  //  val fileStorage = new FileStorage(testFileStorageBasePath, testPrjFileName, testLogFileName, testUsersFileName)

  val mockedUser = mock[User]
  mockedUser.firstName returns "Anders"
  mockedUser.id returns "111111111111"
  val useCaseContext = mock[UseCaseContext]
  useCaseContext.user returns mockedUser

  val mockedStorage = mock[Storage]
  val startdate = "2014-01-01"
  val enddate = "2015-02-28"
  mockedStorage.timeslices(startdate, enddate, useCaseContext) returns Option(TestData.testdata)
  val interactor = new GetTotalTimeInteractor
  //interactor.storage = fileStorage
  interactor.storage = mockedStorage
  interactor.log_=(TestUtil.emptyLog)

  val request = GetTotalTimeRequestModel(startdate, enddate)

  /*
   * TEST
   */
  "GetTotalTimeProjectsInteractor" should {
    "test" in {
      val res = interactor.execute(request, useCaseContext)
//            res.success.foreach(x => {
//              val totRes = x.resultStructure
//              println(dt.getDecimalHours(totRes.totalDuration))
//              totRes.projects foreach { project =>
//                println(project.name + " "
//                  + dt.getDecimalHours(project.duration) + " "
//                  + u.percent(project.duration, totRes.totalDuration, 2) + "%")
//                project.activities foreach { activity =>
//                  println("-> " + activity.name + " "
//                    + dt.getDecimalHours(activity.duration) + " "
//                    + u.percent(activity.duration, totRes.totalDuration, 2) + "%")
//                }
//              }
//            })
      var length = 0
      res.success.foreach { x =>
        length = x.resultStructure.projects.length
      }
      length > 0 must beTrue
    }
  }

}