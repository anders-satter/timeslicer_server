package timeslicer.model.message

/**
 * Each message must have its own copy of 
 * the MessageBuilder so usage is
 * new MessageBuilder().append(...
 * 
 */
class MessageBuilder {
  val messageBuilder = new StringBuilder()
  messageBuilder.append("Timeslicer application")
  messageBuilder.append('\n')
  
  def append(str:String) = {
    messageBuilder.append(str)
  }
  override def toString() = messageBuilder.toString()
}

