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
 * Interface for the FrontController
 */
trait FrontController {
  def perform[R <: RequestModel, S <: ResponseModel](userToken: AuthenticationToken,
                                                     requestModel: R,
                                                     interactor: Interactor[R, S]): Result[S]
}

/**
 * FrontController implementation
 */
class FrontControllerImpl(unAuthorizedAction: Function0[Int])
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
   * Checks authentication and runs the interaction if the user is
   * authenticated, otherwise it will
   */
  def perform[R <: RequestModel, S <: ResponseModel](userToken: AuthenticationToken,
                                                     requestModel: R,
                                                     interactor: Interactor[R, S]): Result[S] = {

    if (SessionManager.sessionExists(userToken.value)) {
      val result: Result[S] =
        interactor.execute(requestModel, createUserContext(SessionManager.session(userToken.value)))
      result
    } else {
      //run the unAuthorized callback
      unAuthorizedAction()
      new Result[S]
    }
  }
}

/**
 * Factory for creating them...
 */
object FrontControllerFactory {

  /**
   * Creation method for a FronController
   */
  def create(unAuthorizedFunction: Function0[Int]): FrontController = new FrontControllerImpl(unAuthorizedFunction)

}