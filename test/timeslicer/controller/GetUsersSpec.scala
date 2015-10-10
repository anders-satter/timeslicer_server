package timeslicer.controller

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import play.api.test.WithApplication
import play.api.test.FakeRequest
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Iteratee
import play.api.libs.iteratee.Input
import play.libs.F._
import java.io.File
import scala.Some

@RunWith(classOf[JUnitRunner])
class GetUsersSpec extends Specification with Mockito {
  "return users" in new WithApplication {
    val users = route(FakeRequest(GET, "/users")).get

    status(users) must equalTo(OK)
    //this should probably be changed to application/json
    //contentType(home) must beSome.which(_ == "text/html")

    users.foreach(list => {
      println(list.body.map { x => println(x) })

    })
    //println(users.)
    contentAsString(users) must contain("users")
  }

  "experiment with enumerators" should {
    "start" in {

      /*
       * https://www.playframework.com/documentation/2.0/Iteratees
       * 
       * The Iteratee is a consumer and describes the way input will be consumed
       * 
       * interface Iteratee[E,A] 
       * E is input that the iteratee accepts
       * A is the calculated result
       * 
       * 3 states showed in the fold implementation:
       * done, cont, error
       * def fold[B](
       *  done: (A, Input[E]) => Promise[B],
       *  cont: (Input[E] => Iteratee[E, A]) => Promise[B],
       *  error: (String, Input[E]) => Promise[B]
       *  ): Promise[B]
       *  
       *  The iteratee has 3 states (fold helps do something useful with each state):
       *  
       *  
       *  1 done: take calculated result of A and what is left from the last consumed 
       *    chunk of input Input[E] and eventually produce a B
       *     
       *  2 cont: the provided continuation is taken and eventually produces a B
       *    this is the only way to push input into the iteratee and get a new iteratee state
       *    using the provided continuation function
       *      
       *  3 error: B is produced by taking the error message (String) and the input that caused it
       *    and then produce a B
       *    
       *    Clarifications
       *    --------------   
       *    Input[E]: chunk of input, can 1 El[E]:actual input, Empty chunk,  EOF:end of stream
       *      for example Input[String] can be El('hello'), Empty or EOF
       * 
       *    Promise[A]: 1) you can schedule a callback to get the value
       *      (check https://www.playframework.com/documentation/2.0/ScalaAsync for async things)
       *    
       *    
       *    By implementing the iteratee (its fold method) we can create some primitive itaratees
       *    that we can later use:
       */

      //      Iteratee[E, A]
      //
      //      def fold[B](
      //        done: (A, Input[E]) => Promise[B],
      //        cont: (Input[E] => Iteratee[E, A]) => Promise[B],
      //        error: (String, Input[E]) => Promise[B]): Promise[B]
      //
      //      val doneIteratee = new Iteratee[String, Int] {
      //        def fold[B](
      //          done: (A, Input[E]) => Promise[B],
      //          cont: (Input[E] => Iteratee[E, A]) => Promise[B],
      //          error: (String, Input[E]) => Promise[B]): Promise[B] = done(1, Input.Empty)
      //      }

      /*
       * Trying to understand Iteratess
       */

      val l = List(1, 2, 3, 4, 5, 6, 7, 8)
      //println(l.foldLeft(0)((total, current) => total + current))

      /*
       * Iteratees can be used to chain iterations and also iterate on other things that 
       * collections... 
       */

      /*
       * Enumerator
       * -----------
       * More generic concept that collections or arrays
       * ex could iterate over something that can produce simple chunks of data
       * available immediately or asynchronously in the future
       * 
       * Statically typed
       * Non-blocking, if nobody consumes the chunks from the enumerator,  
       *
       */

      val stringEnumerator: Enumerator[String] = Enumerator("a", "b", "c")
      //val fileEnumerator: Enumerator[Array[Byte]] = Enumerator.fromFile(new File("myfile.txt"))

      val dateGenerator: Enumerator[String] = Enumerator.generateM(
        play.api.libs.concurrent.Promise.timeout(
          Some("current time %s".format((new java.util.Date()))), 500))
      //println(dateGenerator) //shows the enumerator

      /*
       * A Pizza enumerator
       */
      case class Pizza(val name: String)
      val pizza = Pizza("vesuvio")
      val pizzaEnumerator: Enumerator[Pizza] = Enumerator.enumInput(Input.El(pizza))

      /*
       * This is an iteratee that consumes chunkes of strings and returns an int
       */
      val enumerateUsers: Enumerator[String] = {
        Enumerator("Anders", "Tove", "Samuel")
      }

      /*
       * Iteratee 1
       * ----------
       * Generic translation of the concept of iteration (in pure functional programming)
       * Itaratee 
       *  - a generic entity that waits for an Enumerator to be iterated over 
       *    (Iterator, on the other hand, is built from the collection over which it will iterate)
       *  - created independently of the Enumerator, which is provided to it
       *  - immutable
       *  - stateless
       *  - fully resuable for different enumerators
       *  - so it is applied on an E, or run over an E
       *             -------          -----------
       *  - returns a promise
       */

      /*example*/
      /*First create an Enumerator for ints*/
      val intEnumerator: Enumerator[Int] = Enumerator(1, 2, 3, 4, 5, 6)
      /*then an Interatee*/
      val iteratee = Iteratee.fold(0)((acc: Int, elem: Int) => acc + elem)

      /*this returns a promise...*/
      println(intEnumerator(iteratee))
      /*and so does this*/
      println(intEnumerator.run(iteratee))

      /*
       * Iteratee 2
       * ----------
       * - awaits the first chunk of data immediately after it launches the iteration mechanism
       * - goes on consuming data until computation is finished
       * - inversion of control and dependency injection pattern...
       *    the enumerator provides the data, we apply(or inject) the iteratee to consume
       *    the data 
       * - can really only consume ONE chunk of data...
       * - can perform calculations while it is reading the data
       */

      /*print all consumed chunks*/
      val enumerator = Enumerator(1, 2, 3, 4)
      val iteratee2 = Iteratee.fold(0)((acc: Int, elem: Int) => {
    	  println("total:" +acc + " current elem:" + elem)
        acc + elem

      })
      //enumerator(Iteratee.foreach(x => println(x)))
      enumerator.run(iteratee2)
      ok
      /*
       * Iteratee 3
       * ----------
       * - the three states done, cont and error are also 'State' Iteratees
       * - can be considered a state machine in charge of looping over state
       *    Cont until it detects conditions to switch to terminal states Done or
       *    Error.
       *  
       *  Done(E, A)(a:A, remaining:Input[E])
       *    - a:A the context received from previous step
       *    - remaining: Input[E] representing the next chunk
       *  Error(E)(msg: String, input:Input[E])
       *    - an error message and the input on which it failed
       *  Cont(E,A)(k:Input[E] => Iteratee[E,A])
       *    - from Input[E] to Iteratee[E, A]
       *    - simply a way of consuming one input and returning an Iteratee 
       *      which can consume another input, etc...
       *    
       *  
       */
    }
  }

}