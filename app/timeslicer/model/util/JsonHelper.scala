package timeslicer.model.util
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import play.api.libs.json._
import timeslicer.model.user.User
import timeslicer.model.user.UserImpl
import play.api.libs.json.Json._
import scala.collection.SortedMap

case class JsonUser(firstName: String, lastName: String, id: String, isAuthenticated: Boolean, isAuthorized: Boolean, email: String)
case class UsersContainer(users: Seq[JsonUser])

/**
 * Class converting to json.
 */
object JsonHelper {
  val write = (msg: String) => { msg.toUpperCase() }
  val parse = (str: String) => { Json.parse(str) }

  def jsonUserList(userList: Seq[User]): JsValue = {

    val userJson = toJson(
     Map(          
        "users" -> 
          userList.map(u => {
            Map("firstName" -> toJson(u.firstName),
                "lastName" -> toJson(u.lastName),
                "id" -> toJson(u.id),
                "isAuthorized" -> toJson(u.isAuthorized),
                "isAuthenticated" -> toJson(u.isAuthenticated),
                "email" -> toJson(u.email)
            )
          })))
          
          
    return userJson
  }
  def seqUserList = ???

}


