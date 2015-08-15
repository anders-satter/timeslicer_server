package timeslicer.model.storage.filestorage

import timeslicer.model.storage.Storage
import timeslicer.model.context.UseCaseContext
import timeslicer.model.context.UseCaseContextUtil.validateUseCaseContext
import timeslicer.model.project.Project
import timeslicer.model.project.Activity
import timeslicer.model.timeslice.TimeSlice
import timeslicer.model.user.User
import timeslicer.model.util.FileCommunicationUtil.readFromFileToString
import timeslicer.model.util.FileCommunicationUtil.readFromFileToStringArray
import timeslicer.model.util.FileCommunicationUtil.createUserIdFilesIfNotExists
import timeslicer.model.util.FileCommunicationUtil.saveToFile
import timeslicer.model.util.settings.Settings
import timeslicer.model.project.Project
import scala.collection.mutable.ListBuffer
import timeslicer.model.util.DateTime
import play.api.libs.json.Json
import timeslicer.model.user.UserImpl
import play.api.libs.json.JsString
import timeslicer.model.storage.exception.ItemAlreadyExistsException
import timeslicer.model.storage.exception.InvalidArgumentException
import timeslicer.model.message.MessageBuilder
import java.io.File

/**
 * Text file based implementation of the Storage trait
 * baseFilePath is where the file structures used by this
 * Storage implementation is located, ex filestorage/data
 * users/users.json - depicts all the users in the application
 * users/<userid>/prj.txt - projects/activities for a userid
 * users/<userid>/log.txt - timeslice entries for a userid
 * users/default/log.txt - timeslice entries for the default user
 * users/default/prj.txt - projects/activities for the default user
 */
class FileStorage(baseFilePath: String, projectFileName: String, logFileName: String, usersFileName: String) extends Storage {
  val fsUtil = FileStorageUtil

  /*
   * Path and file name related functions  
   */
  val calcIdPath: (UseCaseContext) => String = useCaseContext => {
    val builder = new StringBuilder()
    builder.append(baseFilePath)
    builder.append('/')
    builder.append("users")
    builder.append('/')
    builder.append(
      createUserIdFilesIfNotExists(
        validateUseCaseContext(useCaseContext).user.id, builder.toString))
    builder.append('/')
    builder.toString()
  }

  val calcProjectFileName: (UseCaseContext) => String = useCaseContext => {
    val builder = new StringBuilder(calcIdPath(useCaseContext))
    builder.append(projectFileName)
    builder.toString()
  }

  val calcLogFileName: (UseCaseContext) => String = useCaseContext => {
    val builder = new StringBuilder(calcIdPath(useCaseContext))
    builder.append(logFileName)
    builder.toString()
  }

  val calcUsersFileName: () => String = () => {
    val builder = new StringBuilder(baseFilePath)
    builder.append('/')
    builder.append("users")
    builder.append('/')
    builder.append(usersFileName)
    builder.toString
  }

  /*
   * Overrides form Storage trait
   */
  override def projects(useCaseContext: UseCaseContext): Option[Seq[Project]] = {
    //println("In projects " + calcProjectFileName(useCaseContext))
    val strSeq = readFromFileToStringArray(calcProjectFileName(useCaseContext), Settings.propertiesMap("ProjectFileEncoding")).toSeq
    var currentProjectName = ""
    var currentActivityName = ""
    var currentActivityList: ListBuffer[Activity] = null
    var projectList: ListBuffer[Project] = new ListBuffer

    def addToProjectList = {
      if (currentProjectName.trim().length() > 0) {
        projectList += Project(currentProjectName, currentActivityList.sortBy(_.name))
      }
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
    val strSeq = readFromFileToStringArray(calcProjectFileName(useCaseContext), Settings.propertiesMap("ProjectFileEncoding")).toSeq

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

  /**
   * Parses a log line into a TimeSlice
   */
  private def parseLogline(logLine: String): Option[TimeSlice] = {
    if (logLine.length() > 0) {
      val parts = logLine.split('\t');
      return Option(TimeSlice(parts(0),
        parts(1),
        parts(3).replaceAll("\"", ""),
        parts(4).replaceAll("\"", ""),
        Option(parts(5).replaceAll("\"", ""))))
    }
    return None
  }

  override def timeslices(start: String, end: String, useCaseContext: UseCaseContext): Option[Seq[TimeSlice]] = {
    val strSeq = readFromFileToStringArray(calcLogFileName(useCaseContext), Settings.propertiesMap("LogFileEncoding")).toSeq
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
       * going from back to the last position
       * of the end date Option
       */
      val endPos = filteredAndSorted.length - (filteredAndSorted.length - 1 to startPos)
        .toStream.takeWhile(i => !filteredAndSorted(i).contains(end)).length

      var foundItems: ListBuffer[TimeSlice] = new ListBuffer
      (startPos to endPos - 1)
        .toStream.map(i => {
          filteredAndSorted(i)
        })
        .foreach(item => {
          parseLogline(item) match {
            case Some(t) => foundItems += t
            case None    => /*do nothing...*/
          }
        })
      return Option(foundItems)
    }
    return None
  }

  def users(): Option[Seq[User]] = {
    val fileContent = readFromFileToString(calcUsersFileName(), Settings.propertiesMap("ProjectFileEncoding"))
    val json = Json.parse(fileContent)
    val fnames = (json \ "users" \\ "firstName").asInstanceOf[ListBuffer[JsString]]
    val lnames = (json \ "users" \\ "lastName").asInstanceOf[ListBuffer[JsString]]
    val userids = (json \ "users" \\ "userId").asInstanceOf[ListBuffer[JsString]]
    val isauths = (json \ "users" \\ "isAuthenticated").asInstanceOf[ListBuffer[JsString]]
    val isauthoz = (json \ "users" \\ "isAuthorized").asInstanceOf[ListBuffer[JsString]]
    val emails = (json \ "users" \\ "email").asInstanceOf[ListBuffer[JsString]]

    val name = fnames(0).asInstanceOf[JsString].value
    val userImpls = for (i <- List.range(0, fnames.length)) yield {
      val user = new UserImpl
      user.firstName = fnames(i).asInstanceOf[JsString].value
      user.lastName = lnames(i).asInstanceOf[JsString].value
      user.id = userids(i).asInstanceOf[JsString].value
      user.isAuthenticated = java.lang.Boolean.valueOf(isauths(i).value)
      user.isAuthorized = java.lang.Boolean.valueOf(isauthoz(i).value)
      user.email = emails(i).asInstanceOf[JsString].value
      user
    }
    return Option(userImpls)
  }

  override def addProject(project: Project, useCaseContext: UseCaseContext): Unit = {
    /*
     * check if the project name already exists
     * if not add the the project name to prj.txt
     */
    this.projects(useCaseContext) match {
      case Some(projSeq) => {
        if (projSeq.filter(p => p.name == project.name).length > 0) {
          //project exists already
          throw new ItemAlreadyExistsException("There is already a project with the name " + project.name)

        } else {
          //add the project
          val fileContent = fsUtil.prepareProjectsForPersistence(projSeq ++ Seq(project))
          saveToFile(calcProjectFileName(useCaseContext), fileContent, false)
        }
      }
      case None => {
        //add the project
        val fileContent = fsUtil.prepareProjectsForPersistence(Seq(project))
        saveToFile(calcProjectFileName(useCaseContext), fileContent, false)
      }
    }
  }

  override def removeProject(project: Project, useCaseContext: UseCaseContext): Unit = {
    val currentProjects: Option[Seq[Project]] = projects(useCaseContext)
    currentProjects match {
      case Some(prjSeq) => {
        val resSeq = fsUtil.performProjectRemoval(project, prjSeq)
        saveToFile(calcProjectFileName(useCaseContext), fsUtil.prepareProjectsForPersistence(resSeq), false)
      }
      case None => /*do nothing, should probably log something*/
    }
  }

  override def addActivity(project: Project, activity: Activity, useCaseContext: UseCaseContext): Unit = {
    val tmpProjects = projects(useCaseContext)
    tmpProjects match {
      case Some(prjSeq) => {
        prjSeq.foreach(p =>
          if (p.name == project.name) {

            /*
             * first check if the activity already exists in the list
             */
            if (p.activityList.filter(a => a.name == activity.name).length > 0) {
              throw new ItemAlreadyExistsException(new MessageBuilder().append("There is already an activity with the name " + activity.name).toString())
            } else {
              p.activityList += activity
            }
          })
        val fileContent = fsUtil.prepareProjectsForPersistence(prjSeq)
        saveToFile(calcProjectFileName(useCaseContext), fileContent, false)
      }
      case None => /*do nothing*/
    }
  }

  override def removeActivity(project: Project, activity: Activity, useCaseContext: UseCaseContext): Unit = {
    projects(useCaseContext) match {
      case Some(projSeq) => {
        projSeq.foreach(p => {
          if (p.name == project.name) {
            if (p.activityList == null) {
              /*do nothing, should probably log here*/
            } else {
              p.activityList.indexWhere(a => a.name == activity.name) match {
                case -1 => /*do nothing*/
                case n => {
                  p.activityList.remove(n)
                  val fileContent = fsUtil.prepareProjectsForPersistence(projSeq)
                  saveToFile(calcProjectFileName(useCaseContext), fileContent, false)
                }
              }
            }
          }
        })
      }
      case None => /*do nothing, should probably do some logging here*/
    }
  }

  override def addTimeSlice(timeslice: TimeSlice, useCaseContext: UseCaseContext): Unit = {
    saveToFile(calcLogFileName(useCaseContext), fsUtil.prepareTimeSliceForPersistence(timeslice), true)
  }
  override def removeTimeSlice(timeslice: TimeSlice, useCaseContext: UseCaseContext): Unit = {
    /*
     * Should we really have an implementation of this?
     * Or should we be able to add 'negative' TimeSlice, 
     * to cancel out a previous one.
     */
  }
  override def addUser(user: User, useCaseContext: UseCaseContext): Unit = {
    /*
     * add user structure to users.json
     */
  }
  override def removeUser(user: User, useCaseContext: UseCaseContext): Unit = {

  }
}

