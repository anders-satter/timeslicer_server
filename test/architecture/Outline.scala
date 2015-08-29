package architecture

import timeslicer.exception.TimeslicerException

object Outline {

  /*
   * DEFINITIONS
   */

  /**
   * Input parameter to the execution
   */
  trait RequestModel
  /*
   * Container of the output
   */
  trait ResponseModel

  /**
   * Result contains the the output and the result model
   * all this could be put in a base framework package
   * so the overview of the architecture could always
   * be shown here
   */
  class Result[S <: ResponseModel] {
    private var _response: Option[S] = None
    private var _errorMessage: String = "No error message"

    def response_=(value: Option[S]): Unit = _response = value
    def errorMessage_=(value: String): Unit = _errorMessage = value

    def get: Either[Option[S], String] = {
      _response match {
        case Some(v) =>
          Left(Some(v))
        case None => Right(_errorMessage)
      }
    }

    def isSuccess: Boolean = {
      _response match {
        case Some(v) => true
        case None    => false
      }
    }

    def getFlat: S = {
      _response match {
        case Some(v) => v
        case None    => throw new TimeslicerException("Result flat value was none!")
      }
    }
  }

  /**
   * The interactor trait
   */
  trait Interactor[R <: RequestModel, Result] {
    def execute(request: R): Result
  }

  /*
   * USECASE IMPLEMENTATIONS
   */
  case class Req(inputValue: String) extends RequestModel
  case class Resp(res: String) extends ResponseModel
  class RespResult extends Result[Resp]
  class WorkInteractor extends Interactor[Req, RespResult] {
    override def execute(r: Req): RespResult = {
      val result = new RespResult

      /*handle error case*/
      if (!r.inputValue.contains("error")) {
        result.response_=(Option(Resp(r.inputValue)))
      } else {
        /*handle error situation*/
        result.errorMessage_=("The input contained the word error")
      }
      result
    }
  }

  /*
   * EXECUTION
   */
  val workInteractor = new WorkInteractor
  workInteractor.execute(Req("error")).get match {
    case Left(v) => v match {
      case Some(o) => println(o)
      case None    => println("none")
    }
    case Right(msg) => println(msg)
  }
  workInteractor.execute(Req("Good!")).get match {
    case Left(v) => v match {
      case Some(o) => println(o)
      case None    => println("none")
    }
    case Right(msg) => println(msg)
  }
//  val result = workInteractor.execute(Req("error!"))
//  result.get.isLeft match {
//    case true => {
//      println(result.getFlat)
//    }
//    case false => println(result.getFlat)
//  }
  val result2 = workInteractor.execute(Req("seldom"))
  result2.get.isLeft match {
    case true => {
      println(result2.getFlat)
    }
    case false => println(result2.getFlat)
  }

//  val resultToPrint = for {
//    res <- workInteractor.execute(Req("hej"))
//    res2 <- Left(res)
//  } yield res
//  println(resultToPrint)
//  
  
  
  
  def main(args: Array[String]): Unit = {}
}