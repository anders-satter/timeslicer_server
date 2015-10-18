package timeslicer.model.util
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import play.api.libs.json._
import timeslicer.model.user.User
import timeslicer.model.user.UserImpl
import play.api.libs.json.Json._
import scala.collection.SortedMap
import timeslicer.model.project.Project

case class JsonUser(userName: String, firstName: String, lastName: String, id: String, isAuthenticated: Boolean, isAuthorized: Boolean, email: String)
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
            Map("userName" -> toJson(u.userName),
              "firstName" -> toJson(u.firstName),
              "lastName" -> toJson(u.lastName),
              "id" -> toJson(u.id),
              "isAuthorized" -> toJson(u.isAuthorized),
              "isAuthenticated" -> toJson(u.isAuthenticated),
              "email" -> toJson(u.email))
          })))

    return userJson
  }
  def seqUserList = ???

  def jsonProjectList(projectList: Seq[Project]): JsValue = {
    val projectJson = toJson(
      Map("projects" ->
        projectList.map(p => {
          Map("name" -> toJson(p.name),
            "activityList" -> toJson(p.activityList.get.map(a => Map("name" -> toJson(a.name)))))
        })))

    return projectJson
  }
}



