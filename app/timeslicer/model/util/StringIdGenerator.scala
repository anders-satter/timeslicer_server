package timeslicer.model.util

import timeslicer.model.util.{ DateTime => dt,Util => u, Math => m }
import scala.collection._

object StringIdGenerator {

  private val allowedChars = "abcdefghijklmnopqrstuvxyz1234567890"

  /**
   * A random user id at the length of 12 characters
   */
  def userId = generator(12)

  /**
   * Returns an id based on the current day and a random number
   */
  def errorId: String = {
    val nowInMillis = String.valueOf(dt.now)
    val nowInNewBase = m.convertBase(nowInMillis,10,16)
    nowInNewBase + "_" + generator(7)()    
  }

  /**
   * Generates a key for the activeuser map
   */
  def activeUserStorageKey = generator(21)

  /**
   * generates a storage key for the sessions in SessionStorage
   */
  def sessionStorageKey = generator(21)

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