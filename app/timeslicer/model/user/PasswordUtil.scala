package timeslicer.model.user

import java.lang.StringBuffer
import java.security.MessageDigest
import java.math.BigInteger
import java.security.SecureRandom
/**
 * Will calculate the sha256 value of a password
 * and does also provide
 */
object PasswordUtil {
  private[this] val random = new SecureRandom

  /**
   * Calculates the sha256 value
   */
  private[this] def sha(str: String): String = {
    val md = java.security.MessageDigest.getInstance("SHA-256")
    val byteArray: Array[Byte] = md.digest(str.getBytes())
    val buff = new StringBuffer
    byteArray.foreach { byte =>
      buff.append(Integer.toString((byte & 0xff) + 0x100, 16)
        .substring(1))
    }
    buff.toString
  }
  /**
   * Non-scala,probably should use iteration
   * but that will have to be redone later...
   */
  private[this] def sha1000(str: String): String = {
    val buff = new StringBuffer
    var res = str
    (0 to 1000).foreach(i => {

      val digest = java.security.MessageDigest.getInstance("SHA-256")
      val byteArray: Array[Byte] = digest.digest(res.getBytes())
      val buff = new StringBuffer
      byteArray.foreach { byte =>
        buff.append(Integer.toString((byte & 0xff) + 0x100, 16)
          .substring(1))
      }
      res = buff.toString
    })
    res
  }

  def createHash(password: String, salt: String): String = {
    sha1000(password + salt)
  }

  def createSalt: String = {
    new SeededRandom().nextBase64String(32)
  }


  def isPasswordValid(password: String): Boolean = {

    return password.length() > 7 &&
      password.filter(x => x.isUpper).length() > 1 &&
      password.filter(c => c.isDigit).length() > 1 &&
      password.filter(c => c.isLower).length() > 1

  }

  //  def main(args: Array[String]): Unit = {
  //    println(createSalt)
  //    val res2 = sha("123456")
  //    println(res2.toString)
  //  }
}
/*
 * from 
 * http://grokbase.com/t/gg/play-framework/146s1cwcp1/secure-random-number-generation-in-playframework
 */
private[this] class SeededRandom {

  private val random = {
    val r = new java.security.SecureRandom()
    // NIST SP800-90A recommends a seed length of 440 bits (i.e. 55 bytes)
    r.setSeed(r.generateSeed(55))
    r
  }

  /**
   * Creates string of random bytes encoded in UTF-8.
   */
  def nextString(numBytes: Int): String = {
    val bytes = new Array[Byte](numBytes)
    random.nextBytes(bytes)
    new String(bytes, "UTF-8")
  }

  /**
   * Return a URL safe base64 string, which may be larger than numBytes.
   */
  def nextBase64String(numBytes: Int): String = {
    val bytes = new Array[Byte](numBytes)
    random.nextBytes(bytes)
    val encodedBytes = java.util.Base64.getUrlEncoder.encode(bytes)
    new String(encodedBytes, "UTF-8")
  }

  def nextLong = random.nextLong()

}
