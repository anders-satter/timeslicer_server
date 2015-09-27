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

  /**
   * Returns the duration of a combination of project and activity,
   * if it is not found, it will return 0
   */
  def getDuration(projectName: String, activityName: String, prjList: Seq[SumProject]): Long =
    prjList.toList
      .find(p => p.name == projectName).getOrElse(new EmptySumProject)
      .activities
      .find(a => a.name == activityName).getOrElse(new EmptySumActivity)
      .duration

  def projectActivityCombinations(prjList: Seq[SumProject]): Seq[Tuple2[String, String]] =
    if (prjList.isEmpty) {
      return Seq()
    } else {
      for {
        p <- prjList
        a <- p.activities
      } yield (p.name, a.name)
    }

}