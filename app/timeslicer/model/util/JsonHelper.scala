package timeslicer.model.util
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import play.api.libs.json._
/**
 * Class converting to json
 */
object JsonHelper {
  val write = (msg: String) => {msg.toUpperCase()}
  val parse = (str:String) => {Json.parse(str)}
  //val validateStr = (value:) => {}
 
  //val validate = (value:JsValue):JsResult[T] => {value.validate[T]}
  //try to construct a validation which can accept any type
  //and validate any type, just for practise
  
 // def validateStrValue(jsValue:JsLookupResult, type:Type):String = {
 //   
 // }
  //
  //Detta är inte vad som kommer att kunna se vad som är det 
  //stora grej
  
}