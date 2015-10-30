package timeslicer.model.usecase.authentication

import timeslicer.model.framework.RequestModel

/**
 * Input is either a user name or an email and a password
 */
case class AuthenticationRequestModel(userName: Option[String], email: Option[String], password: String) extends RequestModel {

  override def toString: String = {
    "UserName:" + userName.getOrElse("") +
      " Email:" + email.getOrElse("") +
      " password:" + (if (password.length() > 0) "******" else "")
  }
}