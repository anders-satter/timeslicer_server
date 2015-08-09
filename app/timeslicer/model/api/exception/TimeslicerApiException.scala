package timeslicer.model.api.exception

import timeslicer.exception.TimeslicerException

/**
 * General api exception
 */
class TimeslicerApiException(override val message: String) extends TimeslicerException(message)