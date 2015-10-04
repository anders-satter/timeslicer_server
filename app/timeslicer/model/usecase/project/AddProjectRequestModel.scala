package timeslicer.model.usecase.project

import timeslicer.model.framework.RequestModel
import timeslicer.model.project.Project

case class AddProjectRequestModel(project:Project) extends RequestModel {
  override def toString: String = {
    return ""
  }

}