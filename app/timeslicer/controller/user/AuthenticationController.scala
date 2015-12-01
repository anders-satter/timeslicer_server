package timeslicer.controller.user

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.mvc.Request
import timeslicer.model.usecase.authentication.AuthenticationRequestModel
import play.api.libs.json.Json
import timeslicer.model.usecase.authentication.AuthenticationRequestModel
import play.api.libs.json.JsValue
import timeslicer.model.usecase.authentication.AuthenticationInteractor
import timeslicer.model.util.Util.EmptyUseCaseContext
import timeslicer.model.usecase.authentication.AuthenticationResponseModel
import timeslicer.model.util.Util.EmptyUseCaseContext
import timeslicer.model.framework.Result
import timeslicer.model.user.NoUser
import timeslicer.model.user.User
import timeslicer.model.context.UseCaseContextImpl

/**
 * Authenticates the user
 * If the user already called another rest service there is already a
 * user and a session.
 * If not we will create a session and a user.
 */
class AuthenticationController extends Controller {
  def user = Action { request =>
    {
      val req = AuthenticationRequestModel
      val json = request.body.asJson.get

      val userName = retrieveParameter("userName", request.body.asJson)
      val email = retrieveParameter("email", request.body.asJson)
      val password = retrieveParameter("password", request.body.asJson)

      val reqModel = AuthenticationRequestModel(Some(userName), Some(email), password)
      
      val interactor = new AuthenticationInteractor
      val res: Result[AuthenticationResponseModel] = interactor.execute(reqModel, new UseCaseContextImpl)

      if (res.isSuccess) {
        Ok
      } else {
        Unauthorized
      }
    }
  }

  def retrieveParameter(parameterName: String, jsonStruct: Option[JsValue]): String = {
    val defaultResultValue = "Not found"
    jsonStruct.map { json =>
      (json \ parameterName).asOpt[String].map { parameterValue =>
        return parameterValue
      }.getOrElse {
        return defaultResultValue
      }
    }
    return defaultResultValue
  }
}