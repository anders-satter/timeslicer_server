package architecture

import scala.util.Try
import scala.util.Success
import scala.util.Failure
import timeslicer.model.context.UseCaseContext
import timeslicer.model.api.Result
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.api.ResponseModel
import scala.util.Failure
import java.sql.Date
import scala.reflect.runtime.{ universe => ru }

object Outline5 extends App {

  /*
   * DEFINITIONS
   */
   
  
  abstract class YInteractor[R <: timeslicer.model.api.RequestModel, S <: timeslicer.model.api.ResponseModel] {
    val PIPE = '|'
    
    def getTime: String = {
      val date = new java.util.Date()
      new java.sql.Timestamp(date.getTime()).toString      
    }

    def pre(r: R) = {
      val buf = new StringBuilder
      buf.append(getTime)
      buf.append(PIPE)
      buf.append("IN")
      buf.append(PIPE)
      buf.append(r.getClass.getName)
      buf.append(PIPE)
      buf.append(r.logInfo)      
      log(buf.toString)
    }

    def post(res: Result[S]) = {
      val buf = new StringBuilder
      buf.append(getTime)
      buf.append(PIPE)
      buf.append("OUT")
      buf.append(PIPE)
      buf.append(res.toString)
      
      res.success.map(s => {        
        buf.append(PIPE)
        buf.append("SUCCESS")
        buf.append(PIPE)
        buf.append(s.getClass.getName)
        buf.append(PIPE)
        buf.append(s.logInfo)
        log(buf.toString)
      })
      
      res.error.map(t => {
    	  buf.append(PIPE)
        buf.append("ERROR")
        buf.append(PIPE)
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
   
  
  
 //abstract class YInteractorImpl extends YInteractor[R,S] 
  
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

  class InteractorTest extends YInteractor[Req, Resp]

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

//  def getTypeTag[T: ru.TypeTag](obj: T) = ru.typeTag[T]
//  //println(getTypeTag(Req).tpe)
//  //println(getTypeTag(Req).tpe.declarations)
//  //println(getTypeTag(interactor).tpe.declarations)
//  val m = ru.runtimeMirror(interactor.getClass.getClassLoader)
//  val im = m.reflect(Resp)
//  println(im)
//getTypeTag: [T](obj: T)(implicit evidence$1: ru.TypeTag[T])ru.TypeTag[T] 
//def main(args: Array[String]): Unit = {}
}