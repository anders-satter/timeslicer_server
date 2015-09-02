package architecture

import scala.util.Try
import scala.util.Success
import scala.util.Failure
import timeslicer.model.context.UseCaseContext
import timeslicer.model.api.Result
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.api.ResponseModel
import scala.util.Failure
import scala.util.Failure
import java.sql.Date

object Outline4 {

  /*
   * DEFINITIONS
   */
  trait YInteractor[R <: timeslicer.model.api.RequestModel, S <: timeslicer.model.api.ResponseModel] {
    val SPACE = " "

    def getTime: String = {
      val date = new java.util.Date()
      new java.sql.Timestamp(date.getTime()).toString
    }

    def pre(r: R) = {
      val buf = new StringBuilder
      buf.append(getTime)
      buf.append(SPACE)
      buf.append("PRE TENTATIVE")
      buf.append(SPACE)
      buf.append(r.getClass.getName)
      buf.append(SPACE)
      buf.append(r.logInfo)
      log(buf.toString)
    }

    def post(res: Result[S]) = {
      val buf = new StringBuilder
      buf.append(getTime)
      buf.append(SPACE)
      buf.append("POST")
      buf.append(SPACE)

      res.success.map(s => {
        buf.append("SUCCESS")
        buf.append(SPACE)
        buf.append(s.getClass.getName)
        buf.append(SPACE)
        buf.append(s.logInfo)
        log(buf.toString)
      })
      res.error.map(t => {
        buf.append("ERROR")
        buf.append(SPACE)
        buf.append(t)
        log(buf.toString)
      })
    }

    def execute(r: R, u: UseCaseContext, f: (R, UseCaseContext) => Result[S]) = {
      pre(r)
      val res = f(r, u)
      post(res)
    }
    def log(msg: Any) = println(msg)
  }

  /*
   * USE CASE IMPLEMENTATION
   */
  case class Req(value: String) extends timeslicer.model.api.RequestModel {
    override def logInfo: String = {
      value.toString
    }
  }
  case class Resp(value: Int) extends timeslicer.model.api.ResponseModel {
    override def logInfo: String = {
      value.toString
    }
  }

  class InteractorTest extends YInteractor[Req, Resp] {}

  val interactor = new InteractorTest
  val useCaseContext: UseCaseContext = new UseCaseContextImpl

  interactor.execute(Req("1"), useCaseContext, (r, u) => {
    //println("In execute anonymous2")
    val result = new Result[Resp]
    Try {
      result.success = Option(Resp(r.value.toInt))
    } match {
      case Failure(e) => result.error = Option(Failure(e))
      case Success(e) =>
    }
    result
  })

  interactor.execute(Req("sjk"), useCaseContext, (r, u) => {
    //println("In execute anonymous2")
    val result = new Result[Resp]
    Try {
      result.success = Option(Resp(r.value.toInt))
    } match {
      case Failure(e) => result.error = Option(Failure(e))
      case Success(e) =>
    }
    result
  })

  def main(args: Array[String]): Unit = {}
}