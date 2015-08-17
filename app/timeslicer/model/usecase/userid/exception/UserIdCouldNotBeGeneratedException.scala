package timeslicer.model.usecase.userid.exception

import timeslicer.exception.TimeslicerException

class UserIdCouldNotBeGeneratedException(message:String) extends TimeslicerException(message){}