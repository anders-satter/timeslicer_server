package timeslicer.model.storage.filestorage

import timeslicer.model.storage.Storage
import timeslicer.model.context.UseCaseContext
import timeslicer.model.project.Project
import timeslicer.model.project.Activity
import timeslicer.model.timeslice.TimeSlice
import timeslicer.model.user.User
import timeslicer.model.util.FileCommunicationUtil
import timeslicer.model.util.settings.Settings
import timeslicer.model.project.Project

class FileStorage(projectFileName: String, logFileName: String) extends Storage {
  val fcu = FileCommunicationUtil

  def projects(useCaseContext: UseCaseContext): Seq[Project] = {
    println(projectFileName)
    val strSeq = fcu.readFromFile(projectFileName, Settings.propertiesMap("ProjectFileEncoding")).toSeq
    return null

  }
  def activities(project: Project, useCaseContext: UseCaseContext): Seq[Activity] = {
    return null
  }
  def timeslices(start: String, end: String, useCaseContext: UseCaseContext): Seq[TimeSlice] = {
    return null
  }
  def users(): Seq[User] = {
    return null
  }
  def addProject(project: Project, useCaseContext: UseCaseContext): Unit = {

  }
  def addActivity(activity: Activity, useCaseContext: UseCaseContext): Unit = {

  }
  def addTimeSlice(timeslice: TimeSlice, useCaseContext: UseCaseContext): Unit = {

  }
}