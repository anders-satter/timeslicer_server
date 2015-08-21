package timeslicer.model.interactor

import timeslicer.model.api.RequestModel
import timeslicer.model.api.ResponseModel
import timeslicer.model.context.UseCaseContext

/**
 * The Interactor is responsible for the main
 * execution of a use case.
 */
trait Interactor[R <: RequestModel, S <: ResponseModel]  {
  def execute(request: R, useCaseContext: UseCaseContext): S
}

