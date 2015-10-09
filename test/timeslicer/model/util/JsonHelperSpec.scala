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
      //println(jsres1)
      ok
    }

    "assert deserialized UserImpl has the same values as the serialized UserImpl - this test is not used for the moment" in {

      //      /*create a user */
      //      val user1 = new UserImpl
      //      user1.firstName = "John"
      //      user1.lastName = "Doe"
      //      user1.id = "111111111111"
      //      user1.email = "abc@email.com"
      //      
      //      /*serialize the user to json*/
      //      val jsonUser = Json.toJson(user1)
      //      //println(jsonUser)
      //      
      //      /*deserialize jsonUser to a user object */
      //      val user2 = jsonUser.as[UserImpl]
      //      user1.firstName == user2.firstName must beTrue
      //      user1.lastName== user2.lastName must beTrue
      //      user1.id == user2.id must beTrue
      //      user1.isAuthenticated == user2.isAuthenticated must beTrue
      //      user1.isAuthorized == user2.isAuthorized must beTrue
      //      user1.email == user2.email must beTrue
      ok
    }

    "userlist test" should {
      "return a json object with users" in {

        val user1 = new UserImpl
        user1.firstName = "Test1"
        user1.lastName = "Testson1"
        user1.id = "111111111111"
        user1.email = "abc1@rt.se"
        user1.isAuthenticated = false
        user1.isAuthorized = true
 
        val user2 = new UserImpl
        user2.firstName = "Test2"
        user2.lastName = "Testson2"
        user2.id = "222222222222"
        user2.email = "abc1@rt.se"
        user2.isAuthenticated = false
        user2.isAuthorized = true

        val user3 = new UserImpl
        user3.firstName = "Test3"
        user3.lastName = "Testson3"
        user3.id = "333333333333"
        user3.email = "abc1@rt.se"
        user3.isAuthenticated = false
        user3.isAuthorized = true

        val user4 = new UserImpl
        user4.firstName = "Test4"
        user4.lastName = "Testson4"
        user4.id = "444444444444"
        user4.isAuthenticated = false
        user4.isAuthorized = true
        
        val userList = Seq(user1, user2, user3, user4)
        
        println(JsonHelper.jsonUserList(userList))
        ok
      }
    }

  }

}