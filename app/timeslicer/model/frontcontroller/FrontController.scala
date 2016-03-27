package timeslicer.model.frontcontroller

import timeslicer.model.authentication.AuthenticationToken
import timeslicer.model.context.UseCaseContext
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.framework.Interactor
import timeslicer.model.framework.RequestModel
import timeslicer.model.framework.ResponseModel
import timeslicer.model.session.SessionManager
import timeslicer.model.session.Session
import timeslicer.model.framework.Result

/**
 * Interface for the FrontController. The FrontController is part of the model
 * to make sure that an interaction only is run by an authenticated user
 */
trait FrontController {
  /**
   * Will first retriev a session from the session model using the
   * authentication token, if none is found the anAuthenticatedAction
   * function, supplied in the implementation is run
   */
  def perform[R <: RequestModel, S <: ResponseModel](userToken: AuthenticationToken,
                                                     requestModel: R,
                                                     interactor: Interactor[R, S]): Result[S]
}

/**
 * FrontController implementation
 */
class FrontControllerImpl
    extends FrontController {
  /**
   * creates the UseCaseContext from the authentication token
   */
  private[this] def createUserContext(session: Session): UseCaseContext = {
    val useCaseContext = new UseCaseContextImpl()
    useCaseContext.sessionId = session.id
    useCaseContext.user = session.user
    useCaseContext
  }

  /**
   * Runs the interaction if the user is authenticated
   */
  def perform[R <: RequestModel, S <: ResponseModel](userToken: AuthenticationToken,
                                                     requestModel: R,
                                                     interactor: Interactor[R, S]): Result[S] = {
    val result: Result[S] =
      interactor.execute(requestModel, createUserContext(SessionManager.session(userToken.value)))
    result
  }
}

/**
 * Factory for creating them...
 */
object FrontControllerFactory {

  /**
   * Creation method for a FronController
   */
  def create: FrontController = new FrontControllerImpl

}