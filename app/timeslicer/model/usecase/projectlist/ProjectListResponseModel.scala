package timeslicer.model.usecase.projectlist

import timeslicer.model.api.ResponseModel
import timeslicer.model.project.Project
import scala.collection.immutable.List

case class ProjectListResponseModel(projectList:Seq[Project]) extends ResponseModel {}