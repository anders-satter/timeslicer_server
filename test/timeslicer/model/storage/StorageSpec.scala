
package timeslicer.model.storage

import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import timeslicer.model.context.UseCaseContext
import timeslicer.model.user.User
import timeslicer.model.project.Project
import timeslicer.model.project.Activity
import timeslicer.model.timeslice.TimeSlice

@RunWith(classOf[JUnitRunner])
class StorageSpec extends Specification with Mockito {
  /*
   * SETUP
   */
  val storage = mock[Storage]
  val user = mock[User]
  user.firstName returns "Anders"
  user.id returns "111111111111"
  val useCaseContext = mock[UseCaseContext]
  useCaseContext.user returns mock[User]

  val projectSeq = Seq(Project("p1", null), Project("p2", null))
  val project = mock[Project]
  project.name returns "Anders"
  val activitySeq = Seq(Activity("act1"), Activity("act2"))
  val usersSeq = Seq(mock[User], mock[User])
  val timeslices = Seq(TimeSlice("","","", "", "", "", None), TimeSlice("", "","", "", "", "", None))

  storage.projects(useCaseContext) returns Option(projectSeq)
  storage.activities(project, useCaseContext) returns Option(activitySeq)
  storage.users() returns Option(usersSeq)
  storage.timeslices("","",useCaseContext) returns Option(timeslices)
  storage.addActivity(project,Activity("act1"), useCaseContext)
  
  

  /*
   * TESTS
   */
  "Storage" should {
    "return projects" in {
      storage.projects(useCaseContext) != null must beTrue
      storage.projects(useCaseContext).get.length > 0 must beTrue
    }
    "return activities" in {
      storage.activities(project, useCaseContext) != null must beTrue
      storage.activities(project, useCaseContext).get.length > 0 must beTrue
    }
    "return users" in {
      storage.users != null must beTrue
      storage.users.get.length > 0 must beTrue
    }
    "add actitivty" in {
      storage.addActivity(project,Activity("act1"), useCaseContext)
      ok
    }
    "add project" in {
    	storage.addProject(Project("proj1", null), useCaseContext)
    	ok
    }
    "add timeslice" in {
    	storage.addTimeSlice(TimeSlice("", "","", "", "", "", None), useCaseContext)
    	ok
    }
    "return FileStorage implementation" in {
      StorageImpl().getClass().getName() == "timeslicer.model.storage.filestorage.FileStorage" must beTrue
    }
  }

}