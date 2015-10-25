package timeslicer.controller.util

import play.api.mvc.Request
import play.api.mvc.AnyContent
import timeslicer.model.autthentication.AuthenticationToken


object RequestUtils {
  def getAuthenticationToken(request:Request[AnyContent]):AuthenticationToken = {
    val value  = request.session.get("AuthenticationId").getOrElse("")
    AuthenticationToken("AuthenticationId",value)   
  }
}