package timeslicer.exception

/*
 * Base exception for this application
 */
class TimeslicerException(val message:String) extends java.lang.RuntimeException {
  override def getMessage():String = {
    "Timeslicer ERROR: " + message + " " + super.getMessage
  }
}