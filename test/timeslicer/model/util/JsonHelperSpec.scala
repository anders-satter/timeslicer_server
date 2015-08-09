package timeslicer.model.util

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

import play.api.libs.json.JsValue
import play.api.libs.json.JsLookupResult
import play.api.libs.json.JsResult
import play.api.libs.json.JsString

/**
 * These are unit tests
 * that we need to run
 */

@RunWith(classOf[JUnitRunner])
class JsonHelperSpec extends Specification {
  val jh = JsonHelper
  val jsonString = """
{ 
  "user": {
    "name" : "toto",
    "age" : 25,
    "email" : "toto@jmail.com",
    "isAlive" : true,
    "friend" : {
      "name" : "tata",
      "age" : 20,
      "email" : "tata@coldmail.com"
    }
  } 
}
"""

  "should convert to json" >> {
    jh.write("hello") == "HELLO"
  }

  "should parse a json string" >> {
    val value: JsValue = jh.parse(jsonString)
    //print(value)
    1 == 1
  }
  "should find name in JsValue" >> {
    //first we find the json structure
    val jsValue1: JsValue = jh.parse(jsonString)

    //then we find the name node in the structure
    //we get a JsLookupResult
    val name: JsLookupResult = jsValue1 \ "user" \ "name"

    //this is an unsafe conversion
    val nameStr: String = (jsValue1 \ "user" \ "name").as[String]
    //println(nameStr)

    /*
     * Using asOpt[T]
     */
    //this is an a safer conversion that will return None if
    //the path returns null
    val nameStr2: Option[String] = (jsValue1 \ "user" \ "name").asOpt[String]
    //println(nameStr2)

    /*
     * Validate[T] converts JsValue[T] to JsResult[T] 
     * JsResult accumulates all detected errors, not just the first one
     * 
     */
    val jsres1:JsResult[Long] = (jsValue1 \ "user" \ "name").validate[Long]

    
    //println(name)
    println(jsres1)
    1 == 1
  }

}