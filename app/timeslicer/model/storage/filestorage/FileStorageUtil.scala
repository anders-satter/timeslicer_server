
package timeslicer.model.storage.filestorage

import timeslicer.model.project.Project

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

}