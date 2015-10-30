package timeslicer.test.util

import timeslicer.model.context.UseCaseContext
import timeslicer.model.framework.Result
import timeslicer.model.util.Util.EmptyUseCaseContext
import timeslicer.model.util.{ DateTime => dt }
import timeslicer.model.user.User

object TestUtil {
  def logBeforeInteraction(caller: Any, r: timeslicer.model.framework.RequestModel, u: UseCaseContext) = ""
  def logAfterInteraction[S <: timeslicer.model.framework.ResponseModel](caller: Any, res: Result[S], u: UseCaseContext) = ""
  def emptyLog: String => Unit = (msg) => {}

  def testUser: User = {
    new User {
      override def userName = "anders"
      override def firstName = "Anders"
      override def lastName = "Sätter"
      override def id = "111111111111"
      override def isAuthenticated = true
      override def isAuthenticated_=(value: Boolean): Unit = {}
      override def isAuthorized = true
      override def isAuthorized_=(value: Boolean): Unit = {}
      override def email = None
      override def validate = true
      override def passwordHash = ""
      override def passwordSalt = ""
    }
  }

}