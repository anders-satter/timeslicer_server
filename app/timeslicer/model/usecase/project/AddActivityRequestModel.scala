package timeslicer.model.usecase.project

import timeslicer.model.framework.RequestModel
import timeslicer.model.project.Activity
import timeslicer.model.project.Project

case class AddActivityRequestModel(val project:Project, val activity:Activity) extends RequestModel {
  override def toString:String = {
    "Project:" + project.name + " Activity:" + activity.name 
  }
}