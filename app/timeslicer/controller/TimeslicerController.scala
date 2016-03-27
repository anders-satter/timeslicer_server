package timeslicer.controller

import play.api.mvc.AnyContent
import play.api.mvc.Controller
import play.api.mvc.Request
import timeslicer.model.framework.Interactor
import timeslicer.model.framework.RequestModel
import timeslicer.model.framework.ResponseModel
import timeslicer.model.framework.Result
import timeslicer.model.frontcontroller.FrontControllerFactory
import timeslicer.model.util.Util.EmptyUseCaseContext

/**
 * Common controller logic, all controllers in the Timeslicer
 * system should inherit from this class
 */
class TimeslicerController extends Controller {

  /**
   * Performs an authenticated action (UseCaseContext is supplied via the AuthenticatedRequest wrapper)
   */
  def performInteraction[R <: RequestModel, S <: ResponseModel](request: AuthenticatedRequest[AnyContent],
                                                                requestModel: R,
                                                                interactor: Interactor[R, S]): Result[S] = {
    val frontController = FrontControllerFactory.create
    return frontController.perform(request.useCaseContext, requestModel, interactor)
  }

  /**
   * Performs an unauthenticated action (no UseCaseContext is supplied)
   */
  def performInteraction[R <: RequestModel, S <: ResponseModel](request: Request[AnyContent],
                                                                requestModel: R,
                                                                interactor: Interactor[R, S]): Result[S] = {
    val frontController = FrontControllerFactory.create
    return frontController.perform(EmptyUseCaseContext(), requestModel, interactor)
  }

}