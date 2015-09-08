package timeslicer.model.util

import timeslicer.model.util.{ Util => u }
import timeslicer.model.util.{ DateTime => dt }

object StringIdGenerator {
  
  
  /**
   * A random user id at the length of 12 characters
   */
  def userId = generator(12)

  /**
   * Returns an id based on the current day and a random number
   */
  def errorId:String = {
    val dayb64 = u.b64EncodeStr(dt.Now(dt.now, dt.getDayValueInStr, dt.fullTimePart).day)
    dayb64 + "_" + generator(7)()
  }

  private val allowedChars = "abcdefghijklmnopqrstuvxyz1234567890"
 
  /**
   * Generates a random string of specified length
   */
  private def generator(length: Int): () => String = {
    def generate: () => String = {
      val builder = new StringBuilder
      val randomGenerator = new java.util.Random
      (0 to length - 1).toStream.foreach(i => {
        val index = randomGenerator.nextInt(allowedChars.length)
        builder.append(allowedChars.charAt(index))
      })
      return builder.toString
    }
    generate
  }

}