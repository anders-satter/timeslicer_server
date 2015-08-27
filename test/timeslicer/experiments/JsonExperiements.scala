package timeslicer.experiments

import play.api.libs.json._
import play.api.libs.functional.syntax._
import timeslicer.model.user.UserImpl

object JsonExperiements {

  /*
   * code from
   * http://reactive.xploregroup.be/blog/13/Play-JSON-in-Scala-intro-and-beyond-the-basics
   */

  val jsonValue = Json.parse("""{"id":1}""")
  val jsonValue2 = Json.parse("""
          {
          "address": {
          "street": "mystreet",
          "city": "mycity"
          }
          }
          """)

  val jsonStr1 = """
     {
       "id":1,
       "name":"file-x",
       "file-type": "jpg"
     }
     
     """

  val city = (jsonValue2 \ "address" \ "city").as[String]
  val id = (jsonValue \ "id").as[Int]

  /*more that one level deep*/

  case class Document(id: Int, name: String, fileType: String)

  /*the reads converter*/
  val documentReader: Reads[Document] = (
    (JsPath \ "id").read[Int] and
    (JsPath \ "name").read[String] and
    (JsPath \ "file-type").read[String])(Document.apply _)

  /*if the reader is not implicit*/
  println("createing a Document from a json string snippet")
  val doc = Json.parse(jsonStr1).as[Document](documentReader)
  println(doc)

  /*the writes converter*/
  val documentWriter: Writes[Document] = (
    (JsPath \ "id").write[Int] and
    (JsPath \ "name").write[String] and
    (JsPath \ "file-type").write[String])(unlift(Document.unapply))

  /*the document to write*/
  println("Writing a document using a writer")
  val doc2 = Document(2, "file-x2", "jpg")
  val json = Json.toJson(doc2)(documentWriter)
  println(json)

  /*document formatter can be used to both write and read*/
  val documentFormatter: Format[Document] = (
    (JsPath \ "id").format[Int] and
    (JsPath \ "name").format[String] and
    (JsPath \ "file-type").format[String])((Document.apply), unlift(Document.unapply))

  val doc3 = Document(3, "file-x3", "jpg")
  val jsValue = Json.toJson(doc3)(documentFormatter)
  val jsValueStr = jsValue.toString

  val doc4 = Json.parse(jsValueStr.toString).as[Document](documentFormatter)

  println("---------------------------")
  println("Using the documentFormatter")
  println(jsValueStr)
  println(doc4)

  println("---------------------------")
  println("writing and reading an object with only one field, cannot do this the ordinary")
  case class OneFieldClass(id: Int)

  /*this stucture is NOT supported*/
  //    val idWriter:Writes[OneFieldClass] = (
  //      (JsPath \ "id").write[Int]
  //  ) (OneFieldClass.apply _)

  val oneFieldClass = OneFieldClass(5)

  println("This only writes the value, and not in a json structure...")
  val oneFieldClassWriter = Writes[OneFieldClass](
    theclass => JsNumber(theclass.id))
  val onefieldJsonStr = Json.toJson(oneFieldClass)(oneFieldClassWriter)
  println(onefieldJsonStr)

  println("using a Json.obj writer")
  val writer = new Writes[OneFieldClass] {
    def writes(foo: OneFieldClass): JsValue = {
      Json.obj("id" -> foo.id)
    }
  }
  println(Json.toJson(oneFieldClass)(writer))

  println("writing three fields with different types")
  case class ThreeFields(id: Int, msg: String, flag: Boolean)
  val tf = ThreeFields(24, "hello", true)

  val writerThreeFields = new Writes[ThreeFields] {
    def writes(c: ThreeFields): JsValue = {
      Json.obj("id" -> c.id,
        "msg" -> c.msg,
        "flag" -> c.flag)
    }
  }
  println(Json.toJson(tf)(writerThreeFields))

  println("reader using the JsResult syntax...")
  val readerThreeFields = new Reads[ThreeFields] {
    def reads(js: JsValue): JsResult[ThreeFields] = {
      val threeFields = ThreeFields(
        (js \ "id").as[Int],
        (js \ "msg").as[String],
        (js \ "flag").as[Boolean])
      return JsSuccess(threeFields)
    }
  }
  val threefieldJson = Json.toJson(tf)(writerThreeFields)
  println(Json.parse(threefieldJson.toString()).as[ThreeFields](readerThreeFields))

  /*using the new validate syntax*/
  println("using the new validate syntax")

  val res: JsResult[ThreeFields] = threefieldJson.validate[ThreeFields](readerThreeFields)
  println(res)
  try {
    val resFail: JsResult[ThreeFields] = onefieldJsonStr.validate[ThreeFields](readerThreeFields)
  } catch {
    case e: Exception => {
      println("This is an known error, so we just print the stack trace")
      //e.printStackTrace()
    }
  }
  println("---------------")

  println("using inception - simplified read and write")
  println("writing...")
  val wr = Json.writes[OneFieldClass]
  val j = Json.toJson(oneFieldClass)(wr)
  println(j)

  println("reading...")
  val re = Json.reads[OneFieldClass]
  val jre = Json.parse(j.toString).as[OneFieldClass](re)
  println(jre)

  println("testing the validation on simplified reads")
  val validation: JsResult[OneFieldClass] = threefieldJson.validate[OneFieldClass](re)
  println(validation)

  println("")
  println("-------------")
  println("complex json object")
  case class Container(users: Seq[OneFieldClass])
  val seq = Seq(OneFieldClass(2), OneFieldClass(8), OneFieldClass(7), OneFieldClass(5))

  //Writes
  val wrO = Json.writes[OneFieldClass]
  implicit val cjwr: Writes[Seq[OneFieldClass]] = Writes.seq(wrO)
  val contwr = Json.writes[Container]

  val cont = Container(seq)
  val complexJson = Json.toJson(cont)(contwr)

  println(complexJson)

  //Reads
  val reO = Json.reads[OneFieldClass]
  implicit val cjre: Reads[Seq[OneFieldClass]] = Reads.seq(reO)
  val contre = Json.reads[Container]
  //val readSeq = Json.parse(complexJson.toString).as[Container](contre)
  println("using validate to initialze the object")
  val readSeq = complexJson.validate[Container](contre)
  println(readSeq)

  def main(args: Array[String]): Unit = {}

}