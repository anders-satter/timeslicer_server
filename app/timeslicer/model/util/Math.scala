package timeslicer.model.util

/**
 * Custom timeslicer Math object.
 */
object Math {
  /**
   * Converts a value from base1 to base2
   */
  def convertBase(value: String, base1: Int, base2: Int): String = java.lang.Long.toString(java.lang.Long.parseLong(value, base1), base2)
}