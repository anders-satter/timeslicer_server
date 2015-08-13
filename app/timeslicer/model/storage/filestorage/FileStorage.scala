package timeslicer.model.storage.filestorage

import timeslicer.model.storage.Storage
import timeslicer.model.context.UseCaseContext
import timeslicer.model.project.Project
import timeslicer.model.project.Activity
import timeslicer.model.timeslice.TimeSlice
import timeslicer.model.user.User
import timeslicer.model.util.FileCommunicationUtil._
import timeslicer.model.util.settings.Settings
import timeslicer.model.project.Project
import scala.collection.mutable.ListBuffer
import timeslicer.model.util.DateTime

/**
 * Text file based implementation of the Storage trait
 */
class FileStorage(projectFileName: String, logFileName: String, usersFileName: String) extends Storage {

  override def projects(useCaseContext: UseCaseContext): Option[Seq[Project]] = {
    val strSeq = readFromFile(projectFileName, Settings.propertiesMap("ProjectFileEncoding")).toSeq
    var currentProjectName = ""
    var currentActivityName = ""
    var currentActivityList: ListBuffer[Activity] = null
    var projectList: ListBuffer[Project] = new ListBuffer

    def addToProjectList = {
      projectList += Project(currentProjectName, currentActivityList.sortBy(_.name))
    }

    strSeq.foreach(item => {
      /*
       * odd thing makes us needing to use contains instead of startsWith
       * since for the first item in the file (#Administration in the test
       * case) position 0 is empty. Oddly this is not visible in an editor 
       */
      if (item.contains("#")) {
        val bracketsPos = item.indexOf("#")
        /*
         * if the currentProjectName has value then we
         * that was set on a previous round and we need
         * to store the project, along with its activityList
         * to the projectList
         */
        if (currentProjectName != "") {
          /*
           * sort the activity list before we add it to the project 
           */
          addToProjectList
        }

        /*
         * this is a project
         */
        currentProjectName = item.substring(bracketsPos + 1, item.length())
        currentActivityList = new ListBuffer()
      } else if (item.contains("+")) {
        /*
         * this is an activity, just add it to the current activity list
         */
        currentActivityList += new Activity(item.substring(1, item.length()))

        /*
         * if this is the last item in strSeq then we need to add the current
         * project and its list to the list of projects
         */
      }
    })
    /*
     * take care of the list item in strSeq...
     */
    addToProjectList

    /*
     * sort the project list
     */
    if (projectList.length > 0) {
      return Option(projectList.sortBy(_.name).toSeq)
    } else {
      return None
    }
  }

  override def activities(project: Project, useCaseContext: UseCaseContext): Option[Seq[Activity]] = {
    val strSeq = readFromFile(projectFileName, Settings.propertiesMap("ProjectFileEncoding")).toSeq

    var pos = strSeq.toStream.takeWhile(item =>
      !item.equals("#" + project.name)).length
    if (pos == strSeq.length) {
      /*
       * we didn't find the project name we asked for
       */
      return None
    } else {
      var activitesForProject: ListBuffer[Activity] = new ListBuffer
      (pos + 1 to strSeq.length - 1)
        .toStream.map(i => strSeq(i))
        .takeWhile(_.startsWith("+"))
        .foreach(item => activitesForProject += Activity(item.substring(1, item.length)))

      if (activitesForProject.length > 0) {
        return Option(activitesForProject.sortBy(_.toString()).toSeq)
      } else {
        return None
      }
    }
  }

  private def parseLogline(logLine: String): TimeSlice = {
    if (logLine.length() > 0) {
      val parts = logLine.split('\t');
      //println(logLine)
      return TimeSlice(parts(0),
        parts(1),
        parts(3).replaceAll("\"", ""),
        parts(4).replaceAll("\"", ""),
        Option(parts(5).replaceAll("\"", "")))
    }
    return null
  }

  override def timeslices(start: String, end: String, useCaseContext: UseCaseContext): Option[Seq[TimeSlice]] = {
    val strSeq = readFromFile(logFileName, Settings.propertiesMap("ProjectFileEncoding")).toSeq
    /*
     * remove all empty lines and sort them
     */
    val filteredAndSorted = strSeq.filter(item => item != null && item.length() > 0).sortBy(_.toString())

    /*
     * select lines within the interval
     * Now that the lines are sorted I suppose
     */
    val startPos = filteredAndSorted.toStream.takeWhile(item => !item.contains(start)).length
    if (startPos == filteredAndSorted.length) {
      /*
       * didn't find start date
       */
      return None
    } else {

      /*
       * Find the last item of end
       * going from back to pos
       */
      val endPos = filteredAndSorted.length - (filteredAndSorted.length - 1 to startPos)
        .toStream.takeWhile(i => !filteredAndSorted(i).contains(end)).length

      var foundItems: ListBuffer[TimeSlice] = new ListBuffer
      (startPos to endPos - 1)
        .toStream.map(i => {
          filteredAndSorted(i)
        })
        .foreach(item => {
          foundItems += parseLogline(item)
        })
      return Option(foundItems)
    }

    return None

  }
  def users(): Option[Seq[User]] = {
    return null
  }
  override def addProject(project: Project, useCaseContext: UseCaseContext): Unit = {

  }
  override def addActivity(activity: Activity, useCaseContext: UseCaseContext): Unit = {

  }
  override def addTimeSlice(timeslice: TimeSlice, useCaseContext: UseCaseContext): Unit = {

  }
}