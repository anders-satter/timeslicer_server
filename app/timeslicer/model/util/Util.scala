package timeslicer.model.util

import java.util.Base64

/**
 * General helper methods
 */
object Util {
  /**
   * Removes citation marks from text items
   */
  def removeCitationMarks(name: String): String = {
    name.replaceAll("\"", "")
  }

  def b64EncodeStr(str: String) = Base64.getEncoder().encodeToString(str.getBytes)
  def b64DecodeStr(str: String) =  new String(Base64.getDecoder().decode(str), "utf-8")

}