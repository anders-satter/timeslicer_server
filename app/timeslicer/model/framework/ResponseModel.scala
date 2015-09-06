package timeslicer.model.framework

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