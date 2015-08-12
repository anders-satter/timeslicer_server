package timeslicer.model.project

import scala.collection.mutable.ListBuffer

/**
 * Holds a project
 */
case class Project(name: String, activityList:ListBuffer[Activity]) {
//  var _activityList: List[Activity] = null
//  def activityList = _activityList
//  def activityList_=(activityList: List[Activity]): Unit = _activityList = activityList
}