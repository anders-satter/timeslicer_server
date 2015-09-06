package architecture

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import timeslicer.model.context.UseCaseContext
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.user.UserImpl
import timeslicer.model.util.{DateTime => dt}
import timeslicer.model.util.StringIdGenerator

object Outline5 extends App {

  /*
   * DEFINITIONS
   */

  /**
   * Implementations for interaction logging functions
   */
  class InteractionLogStringBuilder {
    val PIPE = "|"
    def time(nowMs: Long): dt.Now = {
      dt.Now(dt.now, dt.getDayValueInStr, dt.fullTimePart)
    }

    def logBeforeInteraction(caller: Any, r: timeslicer.model.framework.RequestModel, u: UseCaseContext) = {
      val buf = new StringBuilder
      val t = time(dt.now)
      buf.append(t.day)
      buf.append(PIPE)
      buf.append(t.time)
      buf.append(PIPE)
      buf.append(u.user.id)
      buf.append(PIPE)
      buf.append("PRE")
      buf.append(PIPE)
      buf.append(caller.getClass.getSimpleName)
      buf.append(PIPE)
      buf.append(r.getClass.getSimpleName)
      buf.append(PIPE)
      buf.append(r.logInfo)
      buf.toString
    }

    def logAfterInteraction[S <: timeslicer.model.framework.ResponseModel](caller: Any, res: ResultY[S], u: UseCaseContext) = {
      val buf = new StringBuilder
      val t = time(dt.now)
      buf.append(t.day)
      buf.append(PIPE)
      buf.append(t.time)
      buf.append(PIPE)
      buf.append(u.user.id)
      buf.append(PIPE)
      buf.append("POST")
      buf.append(PIPE)
      buf.append(caller.getClass.getSimpleName)
      buf.append(PIPE)
      buf.append(res.toString)

      res.success.map(s => {
        buf.append(PIPE)
        buf.append("SUCCESS")
        buf.append(PIPE)
        buf.append(s.getClass.getSimpleName)
        buf.append(PIPE)
        buf.append(s.logInfo)
        buf.toString
      })

      res.error.map(c => {
        buf.append(PIPE)
        buf.append("ERROR")
        buf.append(PIPE)
        /*
         * map, foreach doesn't find the error, we have to use
         * get, but if we come here there *must* be an error :)
         * get cannot be used, because this will throw the exception!
         * we just want to read it and the stack trace
         * so we use Failure.failed = Success(Throwable)
         */
        //buf.append(t.failed.get.getStackTrace.mkString("\n"))
        //buf.append(t.failure.failed.get.getStackTrace.mkString)

        buf.append(c.id)
        buf.append(PIPE)
        buf.append(c.failure.get.getStackTrace.mkString("\n"))
      })
      buf.toString
    }
  }

  /**
   * General interactor class to be overridden by specific use case implementations
   */
  abstract class YInteractor[R <: timeslicer.model.framework.RequestModel, S <: timeslicer.model.framework.ResponseModel] {

    /*
     * Default implementations for log functions 
     */
    private var _log: String => Unit = (msg: String) => println(msg)
    private var _beforeLogStringBuilder: (Any, timeslicer.model.framework.RequestModel, UseCaseContext) => String = new InteractionLogStringBuilder().logBeforeInteraction
    private var _afterLogStringBuilder: (Any, ResultY[S], UseCaseContext) => String = new InteractionLogStringBuilder().logAfterInteraction

    /*
     * Setters for log functions, for testing and overriding purposes
     */
    def log_=(f: String => Unit) = _log = f
    def beforeLogStringBuilder_=(f: (Any, timeslicer.model.framework.RequestModel, UseCaseContext) => String) = _beforeLogStringBuilder = f
    def afterLogStringBuilder_=(f: (Any, ResultY[S], UseCaseContext) => String) = _afterLogStringBuilder = f

    /*
     * Hook methods running before and after the interaction    
     */
    def pre(me: Any, r: R, u: UseCaseContext) = _log(_beforeLogStringBuilder(me, r, u))
    def post(me: Any, res: ResultY[S], u: UseCaseContext) = _log(_afterLogStringBuilder(me, res, u))

    /**
     * Execution of the interaction
     */
    def execute(r: R, u: UseCaseContext, f: (R, UseCaseContext) => ResultY[S]) = {
      pre(this, r, u)
      val res = f(r, u)
      post(this, res, u)
    }
  }

  /*
   * USE CASE IMPLEMENTATION
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

  class InteractorTest extends YInteractor[Req, Resp]

  val interactor = new InteractorTest
  val useCaseContext: UseCaseContextImpl = new UseCaseContextImpl
  val user = new UserImpl

  useCaseContext.user = user

  //interactor.loggingFunc { x => println("myFunc: " + x) }
  interactor.execute(Req("1"), useCaseContext, (r, u) => {
    //println("In execute anonymous2")
    val result = new ResultY[Resp]
    Try {
      result.success = Option(Resp(r.value.toInt))
    } match {
      case Failure(e) => result.failure(Failure(e))
      case Success(e) =>
    }
    result
  })

  interactor.execute(Req("failstring"), useCaseContext, (r, u) => {
    //println("In execute anonymous2")
    val result = new ResultY[Resp]
    Try {
      result.success = Option(Resp(r.value.toInt))
    } match {
      //case Failure(e) => result.error = Option(Failure(e))
      case Failure(e) => result.failure(Failure(e))
      case Success(e) =>
    }
    result
  })

  //  val o: Option[Int] = Some(345)
  //  o.foreach(x => println(x))
  //
  //  val s: Success[Int] = Success(567)
  //
  //  //  val t:Try[Throwable] = Failure(new Exception("error"))  
  //  val t: Try[Int] = s
  //  t.map(x => x)

  //  def getTypeTag[T: ru.TypeTag](obj: T) = ru.typeTag[T]
  //  //println(getTypeTag(Req).tpe)
  //  //println(getTypeTag(Req).tpe.declarations)
  //  //println(getTypeTag(interactor).tpe.declarations)
  //  val m = ru.runtimeMirror(interactor.getClass.getClassLoader)
  //  val im = m.reflect(Resp)
  //  println(im)
  //getTypeTag: [T](obj: T)(implicit evidence$1: ru.TypeTag[T])ru.TypeTag[T] 
  //def main(args: Array[String]): Unit = {}

  class ErrorContainer(t: Failure[Throwable], val id: String) {
    /**
     * Get the success of the Failure...
     */
    val failure = t.failed
    val stacktrace = t.failed.map(x => x.getStackTrace.mkString("\n"))
  }

  //  val t = Failure(new Exception("test"))
  //  val ex = new ExceptionContainer(t)
  //  println(ex.id)
  //  println(ex.failure.get)
  //  println(ex.stacktrace)
  
  class ResultY[S <: timeslicer.model.framework.ResponseModel] {
  private var _success: Option[S] = None
  private var _error: Option[Try[Throwable]] = None
  private var _errorContainer: Option[Outline5.ErrorContainer] = None
  def success_=(v: Option[S]): Unit = _success = v
  def failure(fail: Failure[Throwable]): Unit = {
    val errCont = new Outline5.ErrorContainer(fail, StringIdGenerator.errorId)
    _errorContainer = Option(errCont)
  }
  def success = _success
  def error = _errorContainer.map(x => x)
  def logInfo(s: S): String = {
    s.logInfo
  }
}
  
}



