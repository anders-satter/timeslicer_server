package timeslicer.model.util

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.libs.json.JsValue
import play.api.libs.json.JsLookupResult
import play.api.libs.json.JsResult
import play.api.libs.json.JsString
import com.fasterxml.jackson.databind.JsonNode
import timeslicer.model.util.JsonHelper._
import timeslicer.model.user.User
import play.api.libs.json._
import timeslicer.model.user.UserImpl

/**
 * These are unit tests
 * that we need to run
 */

@RunWith(classOf[JUnitRunner])
class JsonHelperSpec extends Specification {

  /*
   * SETUP
   */
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


  /*
   * TEST
   */
  "JsonHelper test" should {

    "Convert to json" in {
      jh.write("hello") == "HELLO"
    }

    "Parse a json string" >> {
      val value: JsValue = jh.parse(jsonString)
      //print(value)
      1 == 1
    }
    "find name in JsValue" >> {
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
      val jsres1: JsResult[Long] = (jsValue1 \ "user" \ "name").validate[Long]

      //println(name)
      println(jsres1)
      1 == 1
    }
    
    "assert deserialized UserImpl has the same values as the serialized UserImpl" in {
      
      /*create a user */
      val user1 = new UserImpl
      user1.firstName = "John"
      user1.lastName = "Doe"
      user1.id = "111111111111"
      user1.email = "abc@email.com"
      
      /*serialize the user to json*/
      val jsonUser = Json.toJson(user1)
      //println(jsonUser)
      
      /*deserialize jsonUser to a user object */
      val user2 = jsonUser.as[UserImpl]
      user1.firstName == user2.firstName must beTrue
      user1.lastName== user2.lastName must beTrue
      user1.id == user2.id must beTrue
      user1.isAuthenticated == user2.isAuthenticated must beTrue
      user1.isAuthorized == user2.isAuthorized must beTrue
      user1.email == user2.email must beTrue      
      //println(jsonUser.as[UserImpl])
    }

  }

}