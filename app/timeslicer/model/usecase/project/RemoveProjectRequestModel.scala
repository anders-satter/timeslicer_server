package timeslicer.model.usecase.project

import timeslicer.model.project.Project
import timeslicer.model.framework.RequestModel

case class RemoveProjectRequestModel(project:Project) extends RequestModel{
  override def toString:String = {
    project.toString
  }
}