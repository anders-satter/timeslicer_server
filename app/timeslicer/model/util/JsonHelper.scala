package timeslicer.model.util
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import play.api.libs.json._
import timeslicer.model.user.User
import timeslicer.model.user.UserImpl


case class JsonUser(firstName: String, lastName: String, id: String, isAuthenticated: Boolean, isAuthorized: Boolean, email: String)
case class UsersContainer(users: Seq[JsonUser])

/**
 * Class converting to json.
 */
object JsonHelper {
  /*
   * So this is where your hiding
   * No body ever did a search
   */
  val write = (msg: String) => { msg.toUpperCase() }
  val parse = (str: String) => { Json.parse(str) }
}