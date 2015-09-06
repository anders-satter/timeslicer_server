package timeslicer.model.framework

import scala.util.Failure
import scala.util.Try
import timeslicer.model.util.StringIdGenerator
import timeslicer.model.context.UseCaseContext
import timeslicer.model.util.{ Util => u, DateTime => dt }

/**
 * General interactor class to be overridden by specific use case implementations
 */
abstract class Interactor[R <: RequestModel, S <: ResponseModel] {

  /*
   * Default implementations for log functions 
   */
  private var _log: String => Unit = (msg: String) => println(msg)
  private var _beforeLogStringBuilder: (Any, RequestModel, UseCaseContext) => String = InteractionLogStringBuilder.logBeforeInteraction
  private var _afterLogStringBuilder: (Any, Result[S], UseCaseContext) => String = InteractionLogStringBuilder.logAfterInteraction

  /*
   * Setters for log functions, for testing and overriding purposes
   */
  def log_=(f: String => Unit) = _log = f
  def beforeLogStringBuilder_=(f: (Any, RequestModel, UseCaseContext) => String) = _beforeLogStringBuilder = f
  def afterLogStringBuilder_=(f: (Any, Result[S], UseCaseContext) => String) = _afterLogStringBuilder = f

  /*
   * Hook methods running before and after the interaction    
   */
  def pre(me: Any, r: R, u: UseCaseContext) = _log(_beforeLogStringBuilder(me, r, u))
  def checkAuthorization(me: Any, r: R, u: UseCaseContext) = {
    //No action yet...  
  }
  def post(me: Any, res: Result[S], u: UseCaseContext) = _log(_afterLogStringBuilder(me, res, u))

  /**
   * Execution of the interaction
   */
  def execute(r: R, u: UseCaseContext) = {
    pre(this, r, u)
    checkAuthorization(this, r, u)
    val res = onExecute(r, u)
    post(this, res, u)
    res
  }

  /**
   * Will do the actual work, is to be overridden by implementors
   */
  def onExecute(r: R, u: UseCaseContext): Result[S]
}

/**
 * Container for Failure thrown in and interaction
 */
class ErrorContainer(t: Failure[Throwable], val id: String) {
  /**
   * Get the success of the Failure...
   */
  val failure = t.failed
  val stacktrace = t.failed.map(x => x.getStackTrace.mkString("\n"))
}

class Result[S <: timeslicer.model.framework.ResponseModel] {
  private var _success: Option[S] = None
  private var _error: Option[Try[Throwable]] = None
  private var _errorContainer: Option[ErrorContainer] = None
  //def success_=(v: Option[S]): Unit = _success = v
  def success_=(v: S): Unit = _success = Option(v)

  def success = _success
  def error = _errorContainer.map(x => x)
  def error_=(fail: Failure[Throwable]): Unit = _errorContainer = Option(new ErrorContainer(fail, StringIdGenerator.errorId))

  def logInfo(s: S): String = {
    s.logInfo
  }
}

  
