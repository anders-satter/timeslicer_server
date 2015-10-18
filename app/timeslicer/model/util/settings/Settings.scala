package timeslicer.model.util.settings

import scala.collection.JavaConverters._
import java.util.Properties
import scala.util.Properties
import scala.io.Source
import java.io.FileInputStream
import scala.collection.mutable.HashMap
import timeslicer.model.util.DateTime
import timeslicer.model.util.Util
/*
 * Reads settings from the settings.properties file
 */

object Settings {

  
  private var _activeUser_inactivityTimeoutDelay: String = ""
  def activeUser_inactivityTimeoutDelay = _activeUser_inactivityTimeoutDelay
  private def activeUser_inactivityTimeoutDelay_=(value: String): Unit = _activeUser_inactivityTimeoutDelay = value
  
  private var _fileStorageBaseFilePath: String = ""
  def fileStorageBaseFilePath = _fileStorageBaseFilePath
  private def fileStorageBaseFilePath_=(value: String): Unit = _fileStorageBaseFilePath = value
  
  private var _logFileName: String = ""
  def logFileName = _logFileName
  private def logFileName_=(value: String): Unit = _logFileName = value

  private var _projectFileName: String = ""
  def projectFileName = _projectFileName
  private def projectFileName_=(value: String): Unit = _projectFileName = value

  private var _settingsFileName: String = ""
  def settingsFileName = _settingsFileName
  private def settingsFileName_=(value: String): Unit = _settingsFileName = value

  private val _propertiesMap: HashMap[Any, String] = new HashMap()
  def propertiesMap = _propertiesMap

  private val _specialWorkdays: HashMap[String, Double] = new HashMap()
  def specialWorkdays = _specialWorkdays

  private var _usersFileName:String = ""
  def usersFileName = _usersFileName
  private def usersFileName_=(value: String): Unit = _usersFileName = value

  def loadProperties = {
    settingsFileName = "settings.properties"
    var properties: Properties = new Properties
    properties.load(new FileInputStream(settingsFileName))
    /*
     * NB the propNames list is only traversable ONCE!
     * 
     * println(propNames) results in: non-empty iterator
     * after foreach:
     * println(propNames) results in: empty iterator
     * 
     */
    val propNames = properties.propertyNames().asScala
    propNames.foreach(item => {
      _propertiesMap += (item -> properties.getProperty(item.toString()))
      if (DateTime.isDay(item.toString)) {
        _specialWorkdays += (item.toString() -> (properties.getProperty(item.toString)).toDouble)
      }
    })
    fileStorageBaseFilePath = propertiesMap("FileStorageBaseFilePath")
    logFileName = propertiesMap("LogFileName")
    projectFileName = propertiesMap("ProjectFileName")
    usersFileName = propertiesMap("UsersFileName")
    activeUser_inactivityTimeoutDelay = propertiesMap("ActiveUser_InactivityTimeoutDelay")
  }

  /*
   * properties are loaded when this class is 
   * accessed the first time
   * It can be reloaded at any time via the loadProperties
   * method   
   */
  loadProperties

  /**
   * Checks against the list NO_CALCULATION_ACTIVITIES in settings.properties
   */
  def isCalculable(activity: String): Boolean = {
    val excludeActivities = propertiesMap("NO_CALCULATION_ACTIVITIES").split(",").map(_.trim()).map(Util.removeCitationMarks(_)).toSet
    !excludeActivities.contains(activity)
  }
}
