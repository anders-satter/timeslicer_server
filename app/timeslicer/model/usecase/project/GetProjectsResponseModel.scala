package timeslicer.model.usecase.project

import timeslicer.model.framework.ResponseModel
import timeslicer.model.project.Project
import scala.collection.immutable.List

case class GetProjectsResponseModel(projectList:Seq[Project]) extends ResponseModel {
  override def toString:String = {
    "project list length:" + projectList.length
  }
}