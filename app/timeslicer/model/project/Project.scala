package timeslicer.model.project

import scala.collection.mutable.ListBuffer

/**
 * Holds a project
 */
//case class Project(val name: String, val activityList:Option[ListBuffer[Activity]])
/*
 * TODO make the activitylist an option!
 */
case class Project(val name: String, val activityList:ListBuffer[Activity])