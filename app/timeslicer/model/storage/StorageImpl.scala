package timeslicer.model.storage

import timeslicer.model.storage.filestorage.FileStorage
import timeslicer.model.util.settings.Settings

/**
 * This object holds the current storage implementation
 */
object StorageImpl {
  val currentStorageImplementation = new FileStorage(Settings.projectFileName,
    Settings.logFileName)
  def apply(): Storage = {
    return currentStorageImplementation
  }
}