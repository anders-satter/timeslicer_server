package timeslicer.model.session

import scala.concurrent.duration.FiniteDuration

trait SessionTimeoutManager {
  def handleTimeoutHandler(storage: SessionStorage, key: String, timeout: FiniteDuration)
  def deleteTimeoutHandler(storage: SessionStorage, key: String)

}