package timeslicer.model.storage

import timeslicer.model.storage.filestorage.FileStorage
import timeslicer.model.util.settings.Settings

/**
 * This object holds the current storage implementation, StorageImpl() should
 * not be called directly in Interaction.onExecute implementations since this
 * make it impossible to mock the storage
 */
object StorageImpl {
  val currentStorageImplementation = new FileStorage(Settings.fileStorageBaseFilePath, Settings.projectFileName,
    Settings.logFileName, Settings.usersFileName)
  def apply(): Storage = {    
    return currentStorageImplementation
  }

}