package timeslicer.model.user.activeuser

import scala.concurrent.duration.FiniteDuration

trait TimeoutManager {
  def handleTimeoutHandler(storage:ActiveUserStorage, key:String, timeout:FiniteDuration)
  def deleteTimeoutHandler(storage:ActiveUserStorage, key:String)
}