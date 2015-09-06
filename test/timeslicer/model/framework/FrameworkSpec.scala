package timeslicer.model.framework

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

import timeslicer.model.context.UseCaseContext
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.user.UserImpl
import timeslicer.model.util.StringIdGenerator

@RunWith(classOf[JUnitRunner])
class FrameworkSpec extends Specification with Mockito {
  /*
   * SETUP
   */
  case class Req(value: String) extends timeslicer.model.framework.RequestModel {
    override def logInfo: String = {
      value.toString
    }
  }
  case class Resp(value: Int) extends timeslicer.model.framework.ResponseModel {
    override def logInfo: String = {
      value.toString
    }
  }
  val interactor = new InteractorTest
  val useCaseContext: UseCaseContextImpl = new UseCaseContextImpl
  val user = new UserImpl
  user.id = StringIdGenerator.userId()
  useCaseContext.user = user

  class InteractorTest extends Interactor[Req, Resp] {
    override def onExecute(r: Req, u: UseCaseContext): Result[Resp] = {
      val res = new Result[Resp]
      Try {
        res.success = Resp(r.value.toInt)
      } match {
        //case Failure(e) => res.failure(Failure(e))
        case Failure(e) => res.error = Failure(e)
        case Success(e) =>
      }
      res
    }
  }
  val interactorTest = new InteractorTest

  /*
   * TEST
   */
  "framework test" should {
    "return succesful value" in {
      interactorTest.execute(Req("1"), useCaseContext)
        .success.map(x => x.value).getOrElse(-1) == 1 must beTrue
    }
    "return failure" in {
    	interactorTest.execute(Req(""), useCaseContext)
    	.error.map(x => x.failure.map(y => y).get).get.getClass
      .getSimpleName.equals("NumberFormatException")  must beTrue
    }
  }
}