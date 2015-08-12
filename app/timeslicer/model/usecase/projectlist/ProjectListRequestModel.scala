package timeslicer.model.usecase.projectlist

import timeslicer.model.api.RequestModel
import timeslicer.model.storage.Storage

case class ProjectListRequestModel(storage:Storage) extends RequestModel {}

