package timeslicer.model.usecase.project

import timeslicer.model.project.Activity
import timeslicer.model.project.Project
import timeslicer.model.framework.RequestModel

case class RemoveActivityRequestModel(project: Project, activity: Activity) extends RequestModel{
  override def toString: String = {
    "Project:" + project.name + " Activity:" + activity.name
  }
}