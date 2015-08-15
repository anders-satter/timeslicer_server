
package timeslicer.model.storage.filestorage

import timeslicer.model.project.Project
import timeslicer.model.timeslice.TimeSlice

object FileStorageUtil {
  def performProjectRemoval(project: Project, projSeq: Seq[Project]): Seq[Project] = {
    projSeq.indexWhere(p => p.name == project.name) match {
      case -1 => projSeq
      case n  => (projSeq take n) ++ (projSeq drop (n + 1))
    }
  }
  def prepareProjectsForPersistence(seq: Seq[Project]): String = {
    var builder = new StringBuilder
    seq.foreach(item => {
      builder.append("#" + item.name)
      builder.append('\n')
      if (item.activityList != null) {
        item.activityList.foreach(a => {
          builder.append("+" + a.name)
          builder.append('\n')
        })
      }
    })
    builder.toString    
  }
  
  def prepareTimeSliceForPersistence(timeslice:TimeSlice):String = {
    val builder = new StringBuilder
    builder.append(timeslice.start)
    builder.append('\t')
    builder.append(timeslice.end)
    builder.append('\t')
    builder.append("0")
    builder.append('\t')
    builder.append('"' + timeslice.project + '"')
    builder.append('\t')
    builder.append('"' + timeslice.activity + '"')
    builder.append('\t')
    timeslice.comment match{
      case Some(c) => {
        builder.append('"' + c + '"')
      }
      case None => builder.append('"' + '"')
    }
    builder.toString
  }

}