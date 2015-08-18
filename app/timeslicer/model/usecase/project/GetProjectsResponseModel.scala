package timeslicer.model.usecase.project

import timeslicer.model.api.ResponseModel
import timeslicer.model.project.Project
import scala.collection.immutable.List

case class GetProjectsResponseModel(projectList:Seq[Project]) extends ResponseModel {}