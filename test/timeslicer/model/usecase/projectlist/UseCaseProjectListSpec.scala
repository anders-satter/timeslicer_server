package timeslicer.model.usecase.projectlist

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.user.User
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UseCaseProjectListSpec extends Specification with Mockito {
  val requestModel = ProjectListRequestModel();  
  val interactor = new ProjectListInteractor
  val useCaseContext = mock[UseCaseContextImpl]
  useCaseContext.user = new User {
     override def name = "Anders"
     override def id = "111111111111"
     override def isAuthorized = true
     override def isAuthenticated = true
     override def email = None
  }
  
  
  
  "ProjectListInteractor" should {
    "return a ProjectListResponseModel" in {      
      interactor.execute(requestModel, useCaseContext)
        .asInstanceOf[ProjectListResponseModel]
        .projectList == null must beFalse     
        interactor.execute(requestModel, useCaseContext)
        .asInstanceOf[ProjectListResponseModel]
        		.projectList.length > 0 must beTrue     
    }
  }
}