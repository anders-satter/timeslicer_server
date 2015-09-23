package timeslicer.model.reporting

import scala.collection.mutable.ListBuffer

object ReportingUtil {
  
  def summarizeSelection(slices: Seq[timeslicer.model.timeslice.TimeSlice]):Seq[SumProject] = {
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
    projectList.toSeq
  }


}