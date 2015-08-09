package timeslicer.model.user

import timeslicer.exception.TimeslicerException
import timeslicer.model.message.ErrorMessageBuilder

class UserImpl extends User {

  private val userNameMaxLength = 30
  private val userNameMinLength = 6
  private val userIdMaxLength = 12
  private val userIdMinLength = 12

  private var _name: String = ""
  private var _id: String = ""
  private var _isAuthenticated: Boolean = false
  private var _isAuthorized: Boolean = false
  private var _email: Option[String] = None

  def name = _name
  def name_=(value: String): Unit = _name = {
    if (value != null && value.length >= 6 && value.length <= 30) {
      value
    } else {
      val messageBuilder = new ErrorMessageBuilder();
      messageBuilder.append("User name must be between " + userNameMinLength + " and " + userNameMaxLength + " in length.")
      messageBuilder.append("Supplied value was: " + value)
      throw new IllegalArgumentException(messageBuilder.toString())
    }
  }

  def id = _id
  def id_=(value: String): Unit = _id = {
    if (value != null && value.length == 12) {
      value
    } else {
      val messageBuilder = new ErrorMessageBuilder();
      messageBuilder.append("User id must be between " + userIdMinLength + " and " + userIdMaxLength + " in length.")
      messageBuilder.append("Supplied value was: " + id)
      throw new IllegalArgumentException(messageBuilder.toString())
    }
  }

  def isAuthenticated = _isAuthenticated
  def isAuthenticated_=(value: Boolean): Unit = _isAuthenticated = value

  def isAuthorized = _isAuthorized
  def isAuthorized_= (value: Boolean) = _isAuthorized = value
  
  def email: Option[String] = _email
  def email_= (email: String): Unit = {
    if (email != null &&
      email.length() > 0
      && email.contains("@")
      && email.contains(".")) {
      _email = Option(email)
    } else {
      throw new IllegalArgumentException(email + " is not a valid email adress")
    }
  }
}