package timeslicer.model.util

object UserIdGenerator {
    val allowedSigns = "abcdefghijklmnopqrstuvxyz1234567890-_!"

  def generate: String = {
    val builder = new StringBuilder
    val randomGenerator = new java.util.Random
    (0 to 11).toStream.foreach(i => {
      val index = randomGenerator.nextInt(allowedSigns.length)
      builder.append(allowedSigns.charAt(index))
    })
    return builder.toString

  }
}