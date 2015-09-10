package timeslicer.model.util

import java.util.Base64
import timeslicer.model.context.UseCaseContext
import timeslicer.model.user.User
import timeslicer.model.user.UserImpl
import timeslicer.model.framework.ResponseModel
import timeslicer.model.framework.RequestModel


/**
 * General helper methods
 */
object Util {
  /**
   * Removes citation marks from text items
   */
  def removeCitationMarks(name: String): String = {
    name.replaceAll("\"", "")
  }

  def b64EncodeStr(str: String) = Base64.getEncoder().encodeToString(str.getBytes)
  def b64DecodeStr(str: String) =  new String(Base64.getDecoder().decode(str), "utf-8")

  class EmptyUseCaseContext extends UseCaseContext {
    override def user:User = {
      val us = new UserImpl
      us.id = "0EMPTY0USER0"
      us
    }
  }
  class EmptyResponseModel extends ResponseModel
  
}