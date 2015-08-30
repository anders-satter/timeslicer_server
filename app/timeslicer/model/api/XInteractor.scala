package timeslicer.model.api

import timeslicer.model.context.UseCaseContext

/**
 * The Interactor is responsible for the main
 * execution of a use case.
 */
trait XInteractor[R <: RequestModel, Result ]  {
  def execute(request: R, useCaseContext: UseCaseContext): Result
}
