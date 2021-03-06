package timeslicer.model.usecase.userid

import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.junit.runner.RunWith
import timeslicer.model.context.UseCaseContext
import timeslicer.model.user.User
import timeslicer.test.util.TestUtil
import timeslicer.model.util.Util.EmptyUseCaseContext

@RunWith(classOf[JUnitRunner])
class UseCaseCreateUserIdSpec extends Specification with Mockito {
  /*
   * SETUP
   */
  val useCaseContext = mock[UseCaseContext]
  val mockedUser = mock[User]
  mockedUser.id returns "222222222222"
  useCaseContext.user returns mockedUser
  val interactor = new CreateUserIdInteractor
  interactor.log_=(TestUtil.emptyLog)

  val requestModel = mock[CreateUserIdRequestModel]

  /*
   * TEST
   */
  "Userid test" should {
    "create a userid" in {
      interactor.execute(requestModel, EmptyUseCaseContext()).success.map(r => r.userId) != null must beTrue

    }

    "create 100 unique userids" in {
      val sortedIdList =
        (0 to 99).toList
          .map(i => interactor.execute(requestModel,EmptyUseCaseContext()).success.map(u => u.userId).getOrElse("0").asInstanceOf[String])
          .sortBy(i => i)

      /*sliding is producing a sliding window over the iterator with the size of 2*/
      /*
       * this will produce an iteraror, since the sliding operation returns this, 
       * thus we need to put all of this into a list to be able to call it more
       * than once
       */
      val duplicates: List[String] =
        (for {
          List(left, right) <- sortedIdList.sliding(2)
          if (left == right)
        } yield left).toList
      
      /*
       * This means that we should have no duplicates...
       */
      duplicates.length < 1 must beTrue
    }
  }
}