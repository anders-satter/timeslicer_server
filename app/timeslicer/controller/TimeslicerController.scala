package timeslicer.controller

import play.api.mvc.Controller
import play.api.mvc.Request
import timeslicer.model.framework.RequestModel
import timeslicer.model.framework.ResponseModel
import timeslicer.model.framework.Interactor
import timeslicer.model.framework.Result
import play.api.mvc.AnyContent
import timeslicer.controller.util.RequestUtils
import timeslicer.model.frontcontroller.FrontControllerFactory
import timeslicer.model.authentication.AuthenticationToken
import play.api.mvc.Action
import timeslicer.model.session.SessionManager

/**
 * Common controller logic, all controllers in the Timeslicer system should inherit from this
 * class
 */
class TimeslicerController extends Controller {

  /**
   * Retrieves the authenticationToken from the request
   */
  def getAuthenticationTokenFromRequest(request: Request[AnyContent]): AuthenticationToken = {
    val value = request.session.get("AuthenticationId").getOrElse("")
    AuthenticationToken("AuthenticationId", value)
  }


  /**
   * This will perform the interaction and return the result
   */
  def performInteraction[R <: RequestModel, S <: ResponseModel](request: Request[AnyContent], requestModel: R, interactor: Interactor[R, S]): Result[S] = {
    val authenticationToken = getAuthenticationTokenFromRequest(request)
    val frontController = FrontControllerFactory.create
    return frontController.perform(authenticationToken, requestModel, interactor)
  }

}