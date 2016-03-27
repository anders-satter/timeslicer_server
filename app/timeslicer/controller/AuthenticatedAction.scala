package timeslicer.controller

import scala.concurrent.Future

import play.api.mvc.ActionBuilder
import play.api.mvc.Request
import play.api.mvc.Result
import play.api.mvc.Results
import play.api.mvc.WrappedRequest
import timeslicer.model.context.UseCaseContext
import timeslicer.model.frontcontroller.FrontController
import timeslicer.model.session.SessionManager

class AuthenticatedRequest[A](val useCaseContext: UseCaseContext,
                              val request: Request[A])
    extends WrappedRequest[A](request)

object AuthenticatedAction extends ActionBuilder[AuthenticatedRequest] {
  def invokeBlock[A](request: Request[A],
                     block: (AuthenticatedRequest[A]) => Future[Result]) = {
    val authId = request.session.get("AuthenticationId").getOrElse("")

    if (!authId.equals("") && SessionManager.sessionExists(authId)) {
      val useCaseContext = FrontController.createUserContext(SessionManager.session(authId))
      block(new AuthenticatedRequest(useCaseContext, request))
    } else {
      Future.successful(Results.Unauthorized)
    }
  }
}
