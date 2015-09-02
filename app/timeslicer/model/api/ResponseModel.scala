package timeslicer.model.api

/**
 * Implementors hold objects of the
 * output of a use case execution
 * (Performed by an Interactor)
 */
trait ResponseModel {
  def logInfo: String = {
    ""
  }

}