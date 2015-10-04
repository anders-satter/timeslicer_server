package timeslicer.model.project

import scala.collection.mutable.ListBuffer
import java.lang.StringBuilder

/**
 * Holds a project
 */
case class Project(val name: String, val activityList: Option[Seq[Activity]]) {
  override def toString: String = {

    val s: StringBuilder = new StringBuilder()   
    s.append("name: " + name )
    s.append(" activities: ")
    activityList.getOrElse(Seq()).foreach(a => s.append(a.name + " "))    
    s.toString
  }
}