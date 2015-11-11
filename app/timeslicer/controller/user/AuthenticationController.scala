package timeslicer.controller.user

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.mvc.Request
import timeslicer.model.usecase.authentication.AuthenticationRequestModel
import play.api.libs.json.Json

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
      println(json)
      val userName = {
        (json \ "userName")
      }
      println(userName.get)

      /**
       * how can I read the request parameters here?
       */

      request.body.asJson.map { json =>
        print("json structure: ")
        println(json)
        (json \ "userName").asOpt[String].map { userName =>
          println("=============")
          println(userName)
        }.getOrElse {
          println("could not find the userName among the parameters")
        }
      }

      //This is not the way to do it, is it
      //println(userName)
      println("================")
      //println(request.body)
      /**
       * so this is the best way to do it all
       */
      Ok(""" {"answer":"Hello"} """)
    }
  }

}