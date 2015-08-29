
package timeslicer.model.storage.filestorage

import timeslicer.model.project.Project
import timeslicer.model.timeslice.TimeSlice
import timeslicer.model.user.User

object FileStorageUtil {

  def matchesEmail(seq: Seq[User], user: User): Boolean = {
    /*
     * email can be an option, so we use flatten to 
     * get the contained values and get rid of all NoneS
     */
    user.email match {
      case Some(e) => seq.toList.map(_.email).flatten.contains(e)
      case None    => false
    }
  }

  def matchesUserName(seq: Seq[User], user: User): Boolean =
    seq
      .toList
      .map(u => u.firstName.trim + u.lastName.trim)
      .contains(user.firstName.trim + user.lastName.trim)

  def matchesId(seq: Seq[User], user: User): Boolean = seq.toList.map(_.id).contains(user.id)

  def performUserRemoval(seq: Seq[User], user: User): Seq[User] = {
    seq.indexWhere(u => user.id == u.id) match {
      case -1 => seq
      case n  => (seq take n) ++ (seq drop (n + 1))
    }
  }

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

  def prepareTimeSliceForPersistence(timeslice: TimeSlice): String = {
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
    timeslice.comment match {
      case Some(c) => {
        builder.append('"' + c + '"')
      }
      case None => builder.append('"' + '"')
    }
    builder.toString
  }
}