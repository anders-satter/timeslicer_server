package timeslicer.controller.user

import play.api.libs.json.JsValue
import play.api.libs.json.JsValue.jsValueToJsLookup
import play.api.mvc.Action
import timeslicer.controller.TimeslicerController
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.framework.Result
import timeslicer.model.usecase.authentication.AuthenticationInteractor
import timeslicer.model.usecase.authentication.AuthenticationRequestModel
import timeslicer.model.usecase.authentication.AuthenticationResponseModel

/**
 * Authenticates the user using the AuthenticationInteractor model, which returns success
 * if the username is in the database and the password is correct.
 */
class AuthenticationController extends TimeslicerController {
  def user = Action { request =>
    {
      val req = AuthenticationRequestModel
      val json = request.body.asJson.get

      /*
       * we will also try to read the session value 
       */
      val userName = retrieveParameter("userName", request.body.asJson)
      val email = retrieveParameter("email", request.body.asJson)
      val password = retrieveParameter("password", request.body.asJson)

      val reqModel = AuthenticationRequestModel(Some(userName), Some(email), password)

      /*
       * The authentication interactor checks that the user is in storage
       * and that the password is the same as the one supplied in the authentication call
       */
      val interactor = new AuthenticationInteractor

      val res: Result[AuthenticationResponseModel] = performInteraction(request, reqModel, interactor)

      if (res.isSuccess) {
        /*
         * Should we look for the database at this position?
         */
        Ok
      } else {
        Unauthorized
      }
    }

  }
  def login = Action { request =>
    {
      val req = AuthenticationRequestModel
      val json = request.body.asJson.get
      //println("json:" + json)
      println("printing the session value");
      println(request.session.get("session"));

      //we will also try to read the session value
      val userName = retrieveParameter("userName", request.body.asJson)
      val email = retrieveParameter("email", request.body.asJson)
      val password = retrieveParameter("password", request.body.asJson)

      val reqModel = AuthenticationRequestModel(Some(userName), Some(email), password)

      // The authentication interactor checks that the user is in the database
      // and that the password is the same as the one supplied in the authentication call 
      val interactor = new AuthenticationInteractor
      val res: Result[AuthenticationResponseModel] = interactor.execute(reqModel, new UseCaseContextImpl)
      if (res.isSuccess) {
        /*
				   * Should we look for the database at this position?
				   */
        Ok
      } else {
        Unauthorized
      }
    }
  }

  /**
   * för att vi inte kommer att kunna se vad det är som gäller idag eller senar
   */
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