package timeslicer.model.reporting;

import scala.collection.mutable.ListBuffer
import timeslicer.model.timeslice.TimeSlice

object ReportingUtil {

  def projectActivityStructure(slices: Seq[TimeSlice], sorted: Boolean = false): Seq[SumProject] = {
    val sortedOnProjects = slices.sortBy(item => item.project)
    val projectMap = sortedOnProjects.groupBy { x => x.project }
    val projectList: ListBuffer[SumProject] = new ListBuffer()
    projectMap.foreach(x => {
      val prjName = x._1
      val sortedActList = x._2.sortBy(i => i.activity)
      val activityMap = sortedActList.groupBy(item => item.activity)
      val activityList: ListBuffer[SumActivity] = new ListBuffer()

      activityMap.foreach { item =>
        activityList += SumActivity(item._1, item._2.toSeq)
      }
      projectList += SumProject(x._1, activityList.toSeq)
    })
    if (!sorted) {
      projectList.toSeq
    } else {
      val sortedList: ListBuffer[SumProject] = new ListBuffer
      projectList.sortBy(p => p.name).foreach(p => {
        sortedList += SumProject(p.name, p.activities.sortBy(a => a.name))
      })
      return sortedList.toSeq
    }
  }

  def getDuration(projectName: String, activityName: String, prjList: Seq[SumProject]): Long = {
//    println(projectName)
//    println(activityName)
    
    
    val item = prjList.toList.filter(p => p.name == projectName).map(p => {
//      p.activities.map(a => {
//        println(a.name)
//        if (a.name == activityName) {
//          a.duration
//        } else {
//          0
//        }
//      })
      val actList = p.activities.filter(a => a.name == activityName)
      if (actList.length > 0) {
        actList(0).duration
      } else {
        0
      }
    })
    //return item(0)(0)
    return item(0)
  }

}