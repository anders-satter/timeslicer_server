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
  def b64DecodeStr(str: String) = new String(Base64.getDecoder().decode(str), "utf-8")

  class EmptyUseCaseContext extends UseCaseContext {
    override def user: User = {
      val us = new UserImpl
      us.id = "0EMPTY0USER0"
      us
    }
  }
  class EmptyResponseModel extends ResponseModel {
    override def toString: String = "Empty"
  }
  class EmptyRequestModel extends RequestModel {
    override def toString: String = "Empty"
  }

  def matchesEmail(seq: Seq[User], user: User): Boolean = {
    /*
     * email can be an option, so we use flatten to 
     * get the contained values and get rid of all NoneS
     */
    user.email match {
      case Some(e) => seq.toList.map(_.email).flatten.contains(e)
      case None    => false
    }
  }

  def matchesUserName(seq: Seq[User], user: User): Boolean =
    seq
      .toList
      .map(u => u.firstName.trim + u.lastName.trim)
      .contains(user.firstName.trim + user.lastName.trim)

  def round(value: Double, decimals: Int): Double = {
    return BigDecimal(value).setScale(decimals, BigDecimal.RoundingMode.HALF_UP).toDouble
  }

  def round2(value: Double) = round(value, 2);

  def percent(x: Long, y: Long, decimals: Int): Double = {
    if (x != 0 && y != 0) {
      return round(x.toDouble / y.toDouble * 100, decimals)
    } else {
      return 0.0
    }
  }
}