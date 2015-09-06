package timeslicer.model.interactor

import timeslicer.model.context.UseCaseContext
import timeslicer.model.framework.RequestModel
import timeslicer.model.framework.ResponseModel


/**
 * The Interactor is responsible for the main
 * execution of a use case.
 */
trait OldInteractor[R <: RequestModel, S <: ResponseModel]  {
  def execute(request: R, useCaseContext: UseCaseContext): S
}