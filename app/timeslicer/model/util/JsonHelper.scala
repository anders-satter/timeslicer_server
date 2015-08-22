package timeslicer.model.util
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import play.api.libs.json._
import timeslicer.model.user.User
import timeslicer.model.user.UserImpl
/**
 * Class converting to json
 */
object JsonHelper {
  val write = (msg: String) => { msg.toUpperCase() }
  val parse = (str: String) => { Json.parse(str) }
  //val validateStr = (value:) => {}

  //val validate = (value:JsValue):JsResult[T] => {value.validate[T]}
  //try to construct a validation which can accept any type
  //and validate any type, just for practise

  // def validateStrValue(jsValue:JsLookupResult, type:Type):String = {

  

}