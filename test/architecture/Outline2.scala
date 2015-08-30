package architecture

import timeslicer.exception.TimeslicerException
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import scala.util.control.Exception._
import scala.util.Failure

object Outline2 {

  /*
   * DEFINITIONS
   */
  /**
   * Input parameter to the execution
   */
  trait RequestModel
  /**
   * Container of the output
   */
  trait ResponseModel

  /**
   * Result contains the the output and the result model
   * all this could be put in a base framework package
   * so the overview of the architecture could always
   * be shown here
   */
  trait Result[S <: ResponseModel] {
    def value: Option[S]
    def error: Option[Try[Throwable]]
  }

  class ResultBase[S <: ResponseModel] {
    private var _success: Option[S] = None
    private var _error: Option[Try[Throwable]] = None
    def success_=(v: Option[S]): Unit = _success = v
    def error_=(v: Option[Try[Throwable]]): Unit = _error = v
    def success = _success
    def error = _error
  }

  //
  /**
   * The interactor trait which executes a use case
   */
  trait Interactor[R <: RequestModel, ResultBase] {
    def execute(request: R): ResultBase
  }

  /*
   * USECASE TEST IMPLEMENTATIONS
   */
  case class Req(value: String) extends RequestModel
  case class Resp(value: Int) extends ResponseModel
  class WorkInteractor extends Interactor[Req, ResultBase[Resp]] {
    override def execute(request: Req): ResultBase[Resp] = {
      val result = new ResultBase[Resp]

      //     val error =  catching(classOf[Throwable]).withTry{
      //         result.success_=(Option(Resp(request.value.toInt)))       
      //      }.failed
      //            catching(classOf[Throwable]).withTry{
      //               result.success_=(Option(Resp(request.value.toInt)))       
      //            }.foreach(x => x)
      //      val c = catching(classOf[NumberFormatException], classOf[Exception]).withApply(e => {
      //        println("heasdjfhalkdjfhak")
      //        result.error_=((Option(Failure(e))))
      //      })
      //      val r = c.opt{
      //        println(request.value)
      //        result.success_=(Option(Resp(request.value.toInt)))
      //      }
      //      result.success_=(c.opt {
      //        //println(request.value)
      //        Option(Resp(request.value.toInt))
      //      }.asInstanceOf[Option[Resp]])

      Try {
        result.success = Option(Resp(request.value.toInt))
        //result.success_=(None)        
      } match {
        case Success(e) => /*do nothing*/
        case Failure(e) => {
          result.error_=(Option(Failure(e)))
        }
      }
      result
    }
  }

  /*
   * EXECUTION
   */
  val workInteractor = new WorkInteractor
  val result = workInteractor.execute(Req(""))

  /*check result with map an foreach*/
  result.success.map(x => {
    x.value * 2
  }).foreach(x => println(x))
  result.error.map(x => {
    x
  }).foreach(x => println(x))
  /*read with default value*/
  println("printing the default value")
  println(result.success.getOrElse(56))

  def main(args: Array[String]): Unit = {}
}
  