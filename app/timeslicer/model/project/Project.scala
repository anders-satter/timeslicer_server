package timeslicer.model.project

import scala.collection.mutable.ListBuffer

/**
 * Holds a project
 */
case class Project(val name: String, val activityList:Option[Seq[Activity]])