package timeslicer.model.usecase.reporting

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import timeslicer.model.storage.filestorage.FileStorage
import timeslicer.model.user.User
import timeslicer.model.context.UseCaseContext
import timeslicer.test.util.TestUtil
import timeslicer.model.storage.Storage
import timeslicer.model.timeslice.TimeSlice

class UseCaseGetTimePerDaySpec extends Specification with Mockito {

  /*
 * Returns a summary structure of projects and activities for each day in the time interval,
 * should facilitate a structure like this
 *
 *  Prj  Act  date1 date2 date3 date4 ... Sum
 *  ---  ---  ----- ----- ----- ----- --- ---
 *  Prj1 Act1   1     1     1     1         4
 *  Prj1 Act2   0     1     3     3         7
 *  Prj2 Act1   0     7     9     3         19
 *  ---  ---  ----- ----- ----- ----- ---  ---
 *  Sum         2     9     10    7         29
 */

  /*
	 * SETUP
	 */
  //  val testFileStorageBasePath = "test/filestorage/data"
  //  val testPrjFileName = "prj.txt"
  //  val testLogFileName = "log.txt"
  //  val testUsersFileName = "users.json"
  //  val fileStorage = new FileStorage(testFileStorageBasePath, testPrjFileName, testLogFileName, testUsersFileName)

  val testdata = Seq(
    TimeSlice("2015-01-01", "10:00", "2015-01-01", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-02", "10:00", "2015-01-02", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-03", "10:00", "2015-01-03", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-04", "10:00", "2015-01-04", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-05", "10:00", "2015-01-05", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-06", "10:00", "2015-01-06", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-07", "10:00", "2015-01-07", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-08", "10:00", "2015-01-08", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-09", "10:00", "2015-01-09", "10:30", "Prj1", "Act1", Option("")),

    TimeSlice("2015-01-01", "10:00", "2015-01-01", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-02", "10:00", "2015-01-02", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-03", "10:00", "2015-01-03", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-04", "10:00", "2015-01-04", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-05", "10:00", "2015-01-05", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-06", "10:00", "2015-01-06", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-07", "10:00", "2015-01-07", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-08", "10:00", "2015-01-08", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-09", "10:00", "2015-01-09", "11:00", "Prj1", "Act2", Option("")),

    TimeSlice("2015-01-01", "10:00", "2015-01-01", "11:00", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-02", "10:00", "2015-01-02", "11:00", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-03", "10:00", "2015-01-03", "11:00", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-04", "10:00", "2015-01-04", "11:00", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-05", "10:00", "2015-01-05", "11:00", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-06", "10:00", "2015-01-06", "11:00", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-07", "10:00", "2015-01-07", "11:00", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-08", "10:00", "2015-01-08", "11:00", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-09", "10:00", "2015-01-09", "11:00", "Prj2", "Act1", Option("")),

    TimeSlice("2015-01-01", "10:00", "2015-01-01", "11:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-02", "10:00", "2015-01-02", "11:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-03", "10:00", "2015-01-03", "11:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-04", "10:00", "2015-01-04", "11:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-05", "10:00", "2015-01-05", "11:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-06", "10:00", "2015-01-06", "11:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-07", "10:00", "2015-01-07", "11:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-08", "10:00", "2015-01-08", "11:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-09", "10:00", "2015-01-09", "11:00", "Prj2", "Act2", Option(""))
    )

  /*user and context*/
  val mockedUser = mock[User]
  mockedUser.firstName returns "Anders"
  mockedUser.id returns "111111111111"
  val useCaseContext = mock[UseCaseContext]
  useCaseContext.user returns mockedUser

  val startdate = "2015-01-01"
  val enddate = "2015-01-10"
  val request = GetTimePerDayRequestModel(startdate, enddate)

  val sortedTestData = testdata.sortBy(_.toString)
  val storage = mock[Storage]
  storage.timeslices(
    startdate, enddate,
    useCaseContext) returns Option(sortedTestData)

  val interactor = new GetTimePerDayInteractor
  interactor.storage = storage
  interactor.log_=(TestUtil.emptyLog)

  /*
   * TEST
   */
  "TimePerDaySpec" should {
    "Do we have test data?" in {
      storage.timeslices(startdate, enddate, useCaseContext).get.length > 0
    }

    "simple execution for the use case" in {
      var result = interactor.execute(request, useCaseContext)
      //println(result.success)
      ok
    }
  }

}