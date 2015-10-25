package timeslicer.model.session.impl

import timeslicer.model.util.settings.Settings
import timeslicer.model.util.settings.Settings
import timeslicer.model.session.SessionStorageProperties

class SessionStoragePropertiesImpl extends SessionStorageProperties {
  override def inactivityTimeoutDelay: Long = Settings.session_inactivityTimeoutDelay.toLong
}
