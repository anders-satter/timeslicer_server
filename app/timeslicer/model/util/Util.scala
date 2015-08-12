package timeslicer.model.util

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
}