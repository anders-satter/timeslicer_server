package timeslicer.model.framework

import scala.util.Failure
import scala.util.Try
import timeslicer.model.util.StringIdGenerator
import timeslicer.model.context.UseCaseContext
import timeslicer.model.util.{ Util => u, DateTime => dt }
import timeslicer.model.util.Util.EmptyUseCaseContext
import timeslicer.model.util.Util.EmptyUseCaseContext
import timeslicer.model.util.Util.EmptyUseCaseContext
import timeslicer.model.util.Util.EmptyResponseModel
import timeslicer.model.util.Util.EmptyRequestModel
import timeslicer.model.storage.StorageImpl
import timeslicer.model.storage.Storage
import timeslicer.model.util.Util.EmptyRequestModel

/**
 * General interactor class to be overridden by specific use case implementations
 */
abstract class Interactor[R <: RequestModel, S <: ResponseModel] {

  /*
   * Default implementations for log functions 
   */
  private[this] var _log: String => Unit = (msg: String) => println(msg)
  private[this] var _beforeLogStringBuilder: (Any, RequestModel, UseCaseContext) => String = InteractionLogStringBuilder.logBeforeInteraction
  private[this] var _afterLogStringBuilder: (Any, Result[S], UseCaseContext) => String = InteractionLogStringBuilder.logAfterInteraction

  /**
   * The default storage is the one set in the
   * apply function of StorageImpl.
   * It can also be set directly on the
   * Interactor implementation, for testing purposes
   * This means that StorageImpl should *NOT* be called
   * directly in the onExecute implementations, since
   * this make them impossible to mock.
   */
  private[this] var _storage: Storage = StorageImpl()
  def storage: Storage = _storage
  def storage_=(s: Storage): Unit = _storage = s

  /*
   * Setters for log functions, for testing and overriding purposes
   */
  
  /**
   * the log getter seems to be necessary to make the syntax  
   * interactor.log = ...
   * working...
   */
  def log:String => Unit = _log
  def log_=(f: String => Unit):Unit = _log = f
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
   * Will do the actual work, is to be overridden by implementors
   * This is the implementation where we have an initialized UseCaseContext
   */
  def onExecute(r: R, u: UseCaseContext): Result[S] = new Result[S]
  def onExecute(r: R): Result[S] = new Result[S]

  /**
   * Execution of the interaction
   */
  def execute(r: R, u: UseCaseContext): Result[S] = {
    pre(this, r, u)
    checkAuthorization(this, r, u)
    val res = onExecute(r, u)
    post(this, res, u)
    res
  }

  /**
   * Execution of the interaction
   * with no UseCaseContext
   */
  def execute(r: R): Result[S] = {
    val u = new EmptyUseCaseContext
    pre(this, r, u)
    checkAuthorization(this, r, u)
    val res = onExecute(r)
    post(this, res, u)
    res
  }

}

/**
 * Container for Failure thrown in an interaction,
 * takes a t:Failure[Throwable], id:String
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

}

  
