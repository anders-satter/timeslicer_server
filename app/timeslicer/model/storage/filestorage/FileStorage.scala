package timeslicer.model.storage.filestorage

import scala.collection.mutable.ListBuffer
import scala.util.Failure
import scala.util.Success
import scala.util.Try
import play.api.libs.json.JsBoolean
import play.api.libs.json.JsLookupResult.jsLookupResultToJsLookup
import play.api.libs.json.JsString
import play.api.libs.json.JsValue.jsValueToJsLookup
import play.api.libs.json.Json
import play.api.libs.json.Writes
import timeslicer.model.context.UseCaseContext
import timeslicer.model.context.UseCaseContextUtil.validateUseCaseContext
import timeslicer.model.project.Activity
import timeslicer.model.project.Project
import timeslicer.model.storage.Storage
import timeslicer.model.storage.StorageFailResult
import timeslicer.model.storage.StorageSuccessResult
import timeslicer.model.storage.exception.ItemAlreadyExistsException
import timeslicer.model.timeslice.TimeSlice
import timeslicer.model.user.User
import timeslicer.model.user.UserImpl
import timeslicer.model.util.FileCommunicationUtil.createUserIdFilesIfNotExists
import timeslicer.model.util.FileCommunicationUtil.readFromFileToString
import timeslicer.model.util.FileCommunicationUtil.readFromFileToStringArray
import timeslicer.model.util.FileCommunicationUtil.saveToFile
import timeslicer.model.util.JsonUser
import timeslicer.model.util.UsersContainer
import timeslicer.model.util.{ Util => u }
import timeslicer.model.util.settings.Settings
import timeslicer.model.storage.StorageSuccessResult
import timeslicer.model.storage.StorageFailResult
import timeslicer.model.util.DateTime

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

    var currentProjectName = ""
    var currentActivityName = ""
    var currentActivityList: ListBuffer[Activity] = null
    var projectList: ListBuffer[Project] = new ListBuffer

    val strSeq = readFromFileToStringArray(calcProjectFileName(useCaseContext),
      Settings.propertiesMap("ProjectFileEncoding")).toSeq

    def addToProjectList = {
      if (currentProjectName.trim().length() > 0) {
        projectList += Project(currentProjectName, Option(currentActivityList.sortBy(_.name).toSeq))
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

    val strSeq = readFromFileToStringArray(calcProjectFileName(useCaseContext),
      Settings.propertiesMap("ProjectFileEncoding")).toSeq

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

      /*
       * split the startday
       */
      val startPart = parts(0).split(" ")
      val endPart = parts(1).split(" ")

      return Option(TimeSlice(
        startPart(0),
        startPart(1),
        endPart(0),
        endPart(1),
        parts(3).replaceAll("\"", ""),
        parts(4).replaceAll("\"", ""),
        Option(parts(5).replaceAll("\"", ""))))
    }
    return None
  }
  override def timeslices(start: String, end: String, useCaseContext: UseCaseContext): Option[Seq[TimeSlice]] = {
    val strSeq = readFromFileToStringArray(calcLogFileName(useCaseContext), Settings.propertiesMap("LogFileEncoding")).toSeq
    val filteredAndSorted = strSeq.toList.filter(item => item != null && item.length() > 0).sortBy(_.toString())
    val selection = filteredAndSorted.filter(i => {
      val curDate = i.split(" ")(0)
      if (curDate.length() > 0) {
        curDate >= start && curDate <= end
      } else {
        false
      }      
    })
    val timeSliceSeq = selection.map(i => parseLogline(i)).flatMap(x => x).toSeq
    return Option(timeSliceSeq)
  }

  def users(): Option[Seq[User]] = {
    val fileContent = readFromFileToString(calcUsersFileName(), Settings.propertiesMap("ProjectFileEncoding"))
    val json = Json.parse(fileContent)
    val fnames = (json \ "users" \\ "firstName").asInstanceOf[ListBuffer[JsString]]
    val lnames = (json \ "users" \\ "lastName").asInstanceOf[ListBuffer[JsString]]
    val userids = (json \ "users" \\ "id").asInstanceOf[ListBuffer[JsString]]
    val isauths = (json \ "users" \\ "isAuthenticated").asInstanceOf[ListBuffer[JsBoolean]]
    val isauthoz = (json \ "users" \\ "isAuthorized").asInstanceOf[ListBuffer[JsBoolean]]
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

  override def addProject(project: Project, useCaseContext: UseCaseContext): Either[StorageFailResult, StorageSuccessResult] = {
    var result: Either[StorageFailResult, StorageSuccessResult] = Right(StorageSuccessResult())
    val prjSeq = this.projects(useCaseContext)
    /*
     * check if the project name already exists
     * if not add the the project name to prj.txt
     */
    prjSeq.getOrElse(Seq()).foreach(item => if (item.name == project.name) {
      return Left(StorageFailResult("There is already a project with the name " + project.name, None))
    })

    /*add the project to the list of projects and persist*/
    val newPrjSeq: Seq[Project] = prjSeq.getOrElse(Seq()) :+ project
    val fileContent = fsUtil.prepareProjectsForPersistence(newPrjSeq)
    Try {
      saveToFile(calcProjectFileName(useCaseContext), fileContent, false)
    } match {
      case Success(s) => return Right(StorageSuccessResult())
      case Failure(e) => return Left(StorageFailResult(e.getMessage, Option(e)))
    }
    return Right(StorageSuccessResult())
  }

  override def removeProject(project: Project, useCaseContext: UseCaseContext): Either[StorageFailResult, StorageSuccessResult] = {
    var result: Either[StorageFailResult, StorageSuccessResult] = Right(StorageSuccessResult())
    val currentProjects: Option[Seq[Project]] = projects(useCaseContext)
    currentProjects match {
      case Some(prjSeq) => {
        val resSeq = fsUtil.performProjectRemoval(project, prjSeq)
        saveToFile(calcProjectFileName(useCaseContext), fsUtil.prepareProjectsForPersistence(resSeq), false)
      }
      case None => result = Left(StorageFailResult("Could not remove project", None))
    }
    result
  }

  override def addActivity(project: Project, activity: Activity, useCaseContext: UseCaseContext): Either[StorageFailResult, StorageSuccessResult] = {
    var result: Either[StorageFailResult, StorageSuccessResult] = Right(StorageSuccessResult())
    val prjSeq = projects(useCaseContext).getOrElse(Seq())
    prjSeq.filter(p => p.name == project.name).foreach(p => {
      if (p.activityList.getOrElse(Seq()).contains(Activity(activity.name))) {
        p.activityList.getOrElse(Seq()) :+ activity
      } else {
        result = Left(StorageFailResult("activity name already exists in the activity list for the project", None))
      }
    })
    val fileContent = fsUtil.prepareProjectsForPersistence(prjSeq)
    saveToFile(calcProjectFileName(useCaseContext), fileContent, false)
    result
  }

  override def removeActivity(project: Project, activity: Activity, useCaseContext: UseCaseContext): Either[StorageFailResult, StorageSuccessResult] = {
    projects(useCaseContext) match {
      case Some(projSeq) => {
        projSeq.foreach(p => {
          if (p.name == project.name) {
            if (p.activityList == null) {
              /*do nothing, should probably log here*/
            } else {
              p.activityList.getOrElse(Seq()).indexWhere(a => a.name == activity.name) match {
                case -1 => /*do nothing*/
                case n => {
                  /*remove through the use of patch(index, replacement, numOfItems)*/
                  p.activityList.getOrElse(Seq()).patch(n, Nil, 1)
                  val fileContent = fsUtil.prepareProjectsForPersistence(projSeq)
                  saveToFile(calcProjectFileName(useCaseContext), fileContent, false)
                }
              }
            }
          }
        })
      }
      case None => Left(StorageFailResult("Couldn't remove Activity", None))
    }
    Right(StorageSuccessResult())
  }

  override def addTimeSlice(timeslice: TimeSlice, useCaseContext: UseCaseContext): Either[StorageFailResult, StorageSuccessResult] = {
    Try {
      saveToFile(calcLogFileName(useCaseContext), fsUtil.prepareTimeSliceForPersistence(timeslice), true)
    } match {
      case Failure(e) => Left(StorageFailResult(e.getMessage, Option(e)))
      case Success(s) => Right(StorageSuccessResult())

    }

  }
  override def removeTimeSlice(timeslice: TimeSlice, useCaseContext: UseCaseContext): Either[StorageFailResult, StorageSuccessResult] = {
    /*
     * Should we really have an implementation of this?
     * Or should we be able to add 'negative' TimeSlice, 
     * to cancel out erroneous ones?.
     */
    return Right(StorageSuccessResult())

  }

  override def addUser(user: User): Either[StorageFailResult, StorageSuccessResult] = {
    var result: (Boolean, String) = (true, "")
    val currentUsersList = FileStorage().users().getOrElse(Seq())

    /**
     * These are really business rules, but are added here to mimic database constraints
     */
    if (u.matchesUserName(currentUsersList, user)) {
      return Left(StorageFailResult("User with name " + user.firstName.trim + " " + user.lastName + " already exists", None))
    }

    if (u.matchesEmail(currentUsersList, user)) {
      return Left(StorageFailResult("User with email " + user.email + " already exists", None))
    }

    /*get all current users and add the new user*/
    val updatedUserList = currentUsersList ++ Seq(user)

    /*convert seq of User to seq of JsonUser*/
    val jsonUserSeq = updatedUserList.map(u => {
      JsonUser(u.firstName,
        u.lastName,
        u.id,
        u.isAuthenticated,
        u.isAuthorized,
        (u.email match {
          case Some(email) => email
          case None        => ""
        }))
    }).toSeq
    /*put the users in the container*/
    val userContainer = UsersContainer(jsonUserSeq)

    /*make a user case class to simplify persisting*/
    val userWrites = Json.writes[JsonUser]
    implicit val userSequenceWrites: Writes[Seq[JsonUser]] = Writes.seq(userWrites)
    val containerWrites = Json.writes[UsersContainer]
    val usersJson = Json.toJson(userContainer)(containerWrites)

    saveToFile(calcUsersFileName(), usersJson.toString, false)
    return Right(StorageSuccessResult())

  }
  /**
   * Removes a user from the data store
   */
  override def removeUser(user: User): Either[StorageFailResult, StorageSuccessResult] = {
    var currentUsersList = (users match {
      case Some(seq) => seq
      case None      => Seq()
    })

    if (fsUtil.matchesId(currentUsersList, user)) {
      val updatedUserList = fsUtil.performUserRemoval(currentUsersList, user)

      /*convert seq of User to seq of JsonUser*/
      val jsonUserSeq = updatedUserList.map(u => {
        JsonUser(u.firstName,
          u.lastName,
          u.id,
          u.isAuthenticated,
          u.isAuthorized,
          (u.email match {
            case Some(email) => email
            case None        => ""
          }))
      }).toSeq
      /*put the users in the container*/
      val userContainer = UsersContainer(jsonUserSeq)

      /*make a user case class to simplify persisting*/
      val userWrites = Json.writes[JsonUser]
      implicit val userSequenceWrites: Writes[Seq[JsonUser]] = Writes.seq(userWrites)
      val containerWrites = Json.writes[UsersContainer]
      val usersJson = Json.toJson(userContainer)(containerWrites)

      /**/
      saveToFile(calcUsersFileName(), usersJson.toString, false)

      Right(StorageSuccessResult())

    } else {
      //println("couldn't ")
      Left(StorageFailResult("couldn't remove user", None))
    }
  }

}

object FileStorage {
  def apply(): FileStorage = {
    return new FileStorage(
      Settings.fileStorageBaseFilePath,
      Settings.projectFileName,
      Settings.logFileName,
      Settings.usersFileName)
  }
}

