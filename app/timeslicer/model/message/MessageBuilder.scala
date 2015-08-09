package timeslicer.model.message

class MessageBuilder {
  val messageBuilder = new StringBuilder()
  messageBuilder.append("Timeslicer application")
  messageBuilder.append('\n')
  
  def append(str:String) = {
    messageBuilder.append(str)
  }
  override def toString() = messageBuilder.toString()
}