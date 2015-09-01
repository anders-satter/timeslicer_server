package timeslicer.model.api

import scala.util.Try

/**
 * Hold the result of an interaction execution
 */
class Result[S <: ResponseModel] {
  private var _success: Option[S] = None
  private var _error: Option[Try[Throwable]] = None
  def success_=(v: Option[S]): Unit = _success = v
  def error_=(v: Option[Try[Throwable]]): Unit = _error = v
  def success = _success
  def error = _error
}