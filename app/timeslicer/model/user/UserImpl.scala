package timeslicer.model.user

import timeslicer.exception.TimeslicerException
import timeslicer.model.message.ErrorMessageBuilder
import play.api.libs.json.Json
import play.api.libs.json.Writes
import play.api.libs.json.Reads._
import play.api.libs.json.Reads
import play.api.libs.json.JsPath
import play.api.libs.functional.syntax._ // Combinator syntax
import scala.util.Try

class UserImpl extends User {

  private val userNameMaxLength = 20
  private val userNameMinLength = 5
  private val userFirstNameMaxLength = 20
  private val userFirstNameMinLength = 1
  private val userLastNameMaxLength = 30
  private val userLastNameMinLength = 1
  private val userIdMaxLength = 12
  private val userIdMinLength = 12

  private var _userName:String = ""
  private var _firstName: String = ""
  private var _lastName: String = ""
  private var _id: String = ""
  private var _isAuthenticated: Boolean = false
  private var _isAuthorized: Boolean = false
  private var _email: Option[String] = None 

  
  def userName = _userName
  def userName_=(value: String): Unit = _userName = {
    if (value != null && value.length >= userNameMinLength && value.length <= userNameMaxLength) {
      value
    } else {
      val messageBuilder = new ErrorMessageBuilder();      
      messageBuilder.append("User name must be between " + userNameMinLength + " and " + userNameMaxLength + " in length.")
      messageBuilder.append("Supplied value was: " + value)
      throw new IllegalArgumentException(messageBuilder.toString())
    }
  }
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
  def isAuthorized_=(value: Boolean) = _isAuthorized = value

  def email: Option[String] = _email
  def email_=(email: String): Unit = {
    if (email != null &&
      email.length() > 0
      && email.contains("@")
      && email.contains(".")) {
      _email = Option(email)
    } else if (email != null && email.length <1){
      _email = None
    } else {   
      throw new IllegalArgumentException(email + " is not a valid email adress")
    }
  }

  override def toString(): String = {
    val buff = new StringBuilder
    buff.append(_userName)
    buff.append('\n')
    buff.append(_firstName)
    buff.append('\n')
    buff.append(_lastName)
    buff.append('\n')
    buff.append(_id)
    buff.append('\n')
    buff.append(_isAuthenticated)
    buff.append('\n')
    buff.append(_isAuthorized)
    buff.append('\n')
    buff.append(_email)
    return buff.toString
  }

  def validate: Boolean = {
    var ret = false
    var results =  scala.collection.mutable.Map[String, Boolean]()

    if (!assertStrValue(_userName,
      userNameMinLength,
      userNameMaxLength)) {
      results += ("userName" -> false)
    } else {
      results += ("userName" -> true)
      ret = true
    }
    if (!assertStrValue(_firstName,
      userFirstNameMinLength,
      userFirstNameMaxLength)) {
      results += ("firstName" -> false)
    } else {
      results += ("firstName" -> true)
      ret = true
    }
    if (!assertStrValue(_lastName,
      userLastNameMinLength,
      userLastNameMaxLength)) {
      results += ("lastName" -> false)
    } else {
      results += ("lastName" -> true)
      ret = true
    }
    if (!assertStrValue(_id,
      userIdMinLength,
      userIdMaxLength)) {
      results += ("id" -> false)
    } else {
      results += ("id" -> true)
      ret = true
    }
    if (assertBooleanValue(_isAuthenticated, false)) {
      results += ("isAuthenticated" -> true)
    } else results += ("isAuthenticated" -> false)

    if (assertBooleanValue(_isAuthorized, false)) {
      results += ("isAuthorized" -> true)
    } else results += ("isAuthorized" -> false)

    _email match {
      case Some(value) => {
        if (!assertStrValue(value,
          userIdMinLength,
          userIdMaxLength)) {
          results += ("id" -> false)
        } else {
          results += ("id" -> true)
          ret = true
        }
      }
      case None => {
        results += ("id" -> true)
      }
    }

    return ret
  }
  
  private def assertStrValue(str: String, min: Int, max: Int): Boolean = {
    return (str != null && str.length() >= min && str.length <= max)
  }

  private def assertBooleanValue(bool: Boolean, expectedValue: Boolean): Boolean = {
    return (bool == expectedValue)
  }
}

//object UserImpl {
//  def apply(firstName: String, lastName: String, id: String, isAuthenticated: Boolean, isAuthorized: Boolean, email: String) = {
//    val user = new UserImpl
//    user._firstName = firstName
//    user._lastName = lastName
//    user._id = id
//    user._isAuthenticated = isAuthenticated
//    user._isAuthorized = isAuthorized
//    user._email = Option(email)
//    user.validate
//    user
//  }
//
//  def applyJson(firstName: String,
//                lastName: String,
//                id: String,
//                isAuthorized: String,
//                isAuthenticated: String,
//                email: String) = {
//    val user = new UserImpl
//    user._firstName = firstName
//    user._lastName = lastName
//    user._id = id
//    user._isAuthenticated = Try(isAuthenticated.toBoolean).getOrElse(false)
//    user._isAuthorized = Try(isAuthorized.toBoolean).getOrElse(false)
//    user._email = Option(email)
//    user.validate
//    user
//  }
//
//  implicit val userImplReads: Reads[UserImpl] = (
//    (JsPath \ "firstName").read[String] and
//    (JsPath \ "lastName").read[String] and
//    (JsPath \ "id").read[String] and
//    (JsPath \ "isAuthorized").read[String] and
//    (JsPath \ "isAuthenticated").read[String] and
//    (JsPath \ "email").read[String])(UserImpl.applyJson _)
//}