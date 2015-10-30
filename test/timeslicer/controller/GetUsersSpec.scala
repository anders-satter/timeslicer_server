package timeslicer.controller

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.libs.iteratee.Cont
import play.api.libs.iteratee.Done
import play.api.libs.iteratee.Input
import play.api.libs.iteratee.Iteratee
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers.GET
import play.api.test.Helpers.OK
import play.api.test.Helpers.defaultAwaitTimeout
import play.api.test.Helpers.route
import play.api.test.Helpers.status
import play.api.test.Helpers.writeableOf_AnyContentAsEmpty
import play.api.test.WithApplication
import org.specs2.runner.JUnitRunner
import scala.util.Try
import play.api.libs.iteratee.Enumerator
import scala.concurrent.impl.Promise
import scala.concurrent.Promise

@RunWith(classOf[JUnitRunner])
class GetUsersSpec extends Specification with Mockito {
  println("PLAY VERSION:" + play.core.PlayVersion.current)

  "return users" in new WithApplication {
    val users: Future[Result] = route(FakeRequest(GET, "/timeslicer/users")).get
    
    status(users) must equalTo(OK)
    //this should probably be changed to application/json
    //contentType(home) must beSome.which(_ == "text/html")

    /*users*/
    val r1: Try[Result] = users.value.get
    r1 match {
      case Success(result: Result) => {
        val body: Enumerator[Array[Byte]] = result.body
        /*The 'run' method on the body returns a Future with the byte array */
        val abf: Future[Array[Byte]] = body.run(usersIteratee)
        /*Future.value returns an Option[Try[Array[Byte]]]*/
        val res: Try[Array[Byte]] = abf.value.get
        val bytes: Array[Byte] = res.get

        val jsonString = new String(bytes, "UTF-8")
        println("body---->" + jsonString)
      }
      case Failure(e) => println(e)
    }
  }
  
  /**
   * An Iteratee to collect all bytes from the result body
   * 
   */
  def usersIteratee: Iteratee[Array[Byte], Array[Byte]] = {
    //Inner step function
    def step(idx: Int, total: Array[Byte])(ab: Input[Array[Byte]]): Iteratee[Array[Byte], Array[Byte]] = ab match {
      case Input.EOF | Input.Empty => Done(total, Input.EOF)
      /*++ is the array concatenation operator*/
      case Input.El(e)             => Cont[Array[Byte], Array[Byte]](i => step(idx + 1, total ++ e)(i))
    }
    Cont[Array[Byte], Array[Byte]](i => step(0, Array())(i))
  }

}