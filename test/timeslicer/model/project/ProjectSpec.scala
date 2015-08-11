package timeslicer.model.project

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner


@RunWith(classOf[JUnitRunner])
class ProjectSpec extends Specification with Mockito{
  val project = Project("Project1")
  "Project" should {
//    "take a name" in {
//      project.name = "Project1"
//      ok
//    }
    "take an activity list" in {
      project.activityList = List(Activity("Activity1"), 
          Activity("Activity2"), 
          Activity("Activity3"), 
          Activity("Activity4"))
      ok
    }
  }
}