package timeslicer.model.framework

import scala.util.Failure
import scala.util.Try
import scala.util.control.NonFatal

import play.api.Logger
import timeslicer.model.context.UseCaseContext
import timeslicer.model.storage.Storage
import timeslicer.model.storage.StorageImpl
import timeslicer.model.util.StringIdGenerator

/**
 * General interactor class to be overridden by specific use case implementations
 */
abstract class Interactor[R <: RequestModel, S <: ResponseModel] {
  val interactionLogger = Logger("timeslicer_interaction")
  val errorLogger = Logger("timeslicer_error")
  /*
   * Default implementations for log functions 
   */
  private[this] var _log: String => Unit = (msg: String) => interactionLogger.info(msg)
  private[this] var _beforeLogStringBuilder: (Any, RequestModel, UseCaseContext, String) => String = InteractionLogStringBuilder.logBeforeInteraction
  private[this] var _afterLogStringBuilder: (Any, Result[S], UseCaseContext, String) => String = InteractionLogStringBuilder.logAfterInteraction
  private[this] var _errorLogStringBuilder: (Any, UseCaseContext, Throwable, String) => String = InteractionLogStringBuilder.logAtError

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
  
  
  /**
   * InteractionIdManager manages the 
   */  
  private[this] var _interactionIdManager:InteractionIdManager = InteractionIdManager()
  def interactionIdManager = _interactionIdManager
  def interactionIdManager_=(iidm:InteractionIdManager) = _interactionIdManager = iidm
  
  
  /*
   * Setters for log functions, for testing and overriding purposes
   */
  def log: String => Unit = _log
  def log_=(f: String => Unit): Unit = _log = f
  def beforeLogStringBuilder_=(f: (Any, RequestModel, UseCaseContext, String) => String) = _beforeLogStringBuilder = f
  def afterLogStringBuilder_=(f: (Any, Result[S], UseCaseContext, String) => String) = _afterLogStringBuilder = f
  def errorLogStringBuilder = _errorLogStringBuilder

  /*
   * Hook methods running before and after the interaction    
   */
  def pre(me: Any, r: R, u: UseCaseContext, interactionId:String) = _log(_beforeLogStringBuilder(me, r, u, interactionId))
  def checkAuthorization(me: Any, r: R, u: UseCaseContext) = {
    //No action yet...  
  }
  def post(me: Any, res: Result[S], u: UseCaseContext, interactionId:String) = _log(_afterLogStringBuilder(me, res, u, interactionId))

  /**
   * Will do the actual work, is to be overridden by implementors
   * This is the implementation where we have an initialized UseCaseContext
   * No implementation will force extension classes to override this
   * method
   */
  def onExecute(r: R, u: UseCaseContext): Result[S]

  /**
   * Execution of the interaction
   */
  def execute(r: R, u: UseCaseContext): Result[S] = {
    var res = new Result[S]
    val interactionId = InteractionIdManager().interactionId(u.sessionId)
    try {
      pre(this, r, u, interactionId)
      checkAuthorization(this, r, u)
      res = onExecute(r, u)
    } catch {
      case NonFatal(e) => {
        /*breaking error*/
        val errorId = StringIdGenerator.errorId
        errorLogger.error(_errorLogStringBuilder(this, u, e, errorId))
        res.failure = "ERROR|" + errorId
      }
    } finally {
      post(this, res, u, interactionId)
    }
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

case class FailureContainer(message: String)

class Result[S <: timeslicer.model.framework.ResponseModel] {
  private[this] var _success: Option[S] = None
  private[this] var _error: Option[Try[Throwable]] = None
  private[this] var _errorContainer: Option[ErrorContainer] = None
  private[this] var _failure: Option[FailureContainer] = None

  def success_=(v: S): Unit = _success = Option(v)
  def success = _success

  def failure_=(message: String): Unit = _failure = Option(FailureContainer(message))
  def failure = _failure

  def error = _errorContainer.map(x => x)
  def error_=(fail: Failure[Throwable]): Unit = _errorContainer = Option(new ErrorContainer(fail, StringIdGenerator.errorId))

}
  
