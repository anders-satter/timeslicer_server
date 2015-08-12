package timeslicer.model.user

import timeslicer.exception.TimeslicerException
import timeslicer.model.message.ErrorMessageBuilder

class UserImpl extends User {

  private val userFirstNameMaxLength = 20
  private val userFirstNameMinLength = 1
  private val userLastNameMaxLength = 30
  private val userLastNameMinLength = 1
  private val userIdMaxLength = 12
  private val userIdMinLength = 12

  private var _firstName: String = ""
  private var _lastName: String = ""
  private var _id: String = ""
  private var _isAuthenticated: Boolean = false
  private var _isAuthorized: Boolean = false
  private var _email: Option[String] = None

  def firstName = _firstName
  def firstName_=(value: String): Unit = _firstName = {
    if (value != null && value.length >= userFirstNameMinLength && value.length <= userFirstNameMaxLength) {
      value
    } else {
      val messageBuilder = new ErrorMessageBuilder();
      messageBuilder.append("User name must be between " + userFirstNameMinLength + " and " + userFirstNameMaxLength + " in length.")
      messageBuilder.append("Supplied value was: " + value)
      throw new IllegalArgumentException(messageBuilder.toString())
    }
  }
  def lastName = _lastName
  def lastName_=(value: String): Unit = _lastName = {
		  if (value != null && value.length >= userLastNameMinLength && value.length <= userLastNameMaxLength) {
			  value
		  } else {
			  val messageBuilder = new ErrorMessageBuilder();
			  messageBuilder.append("User name must be between " + userLastNameMinLength + " and " + userLastNameMaxLength + " in length.")
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