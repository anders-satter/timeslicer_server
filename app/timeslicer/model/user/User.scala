package timeslicer.model.user

import play.api.libs.json.Writes
import play.api.libs.json.Json
import play.api.libs.json.Reads._
import play.api.libs.json.Reads
import play.api.libs.json.JsPath
import play.api.libs.functional.syntax._ // Combinator syntax

/**
 * User of the
 */
trait User {
  /**
   * The users chosen username for the application
   */
  def userName:String
  def firstName: String
  def lastName: String
  def id: String
  def isAuthenticated: Boolean
  def isAuthorized: Boolean
  /**
   * Email is optional so...
   */
  def email: Option[String]
  def validate:Boolean
  def passwordHash:String
  def passwordSalt:String
}

/**
 * Companion object added to make room for a
 * json writes for a user object
 */
object User {

  implicit val userWrights = new Writes[User] {
    def writes(user: User) = Json.obj(
      "userName" -> user.userName,
      "firstName" -> user.firstName,
      "lastName" -> user.lastName,
      "id" -> user.id,
      "isAuthenticated" -> String.valueOf(user.isAuthenticated),
      "isAuthorized" -> String.valueOf(user.isAuthorized),
      "email" -> (user.email match {
        case Some(e) => e
        case None    => ""
      }))
  }
}