package timeslicer.model.context

import timeslicer.model.storage.exception.InvalidArgumentException
import timeslicer.model.message.MessageBuilder

object UseCaseContextUtil {

  def validateUseCaseContext(useCaseContext: UseCaseContext): UseCaseContext = {
    if (useCaseContext.user == null) {
      throw new InvalidArgumentException(new MessageBuilder()
        .append("User is missing in UseCaseContext").toString)
    }
    if (useCaseContext.user.id == null) {
      throw new InvalidArgumentException(new MessageBuilder()
        .append("UserId is missing in User").toString)
    }
    return useCaseContext
  }  
}