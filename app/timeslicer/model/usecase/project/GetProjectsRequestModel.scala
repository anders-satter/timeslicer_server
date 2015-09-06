package timeslicer.model.usecase.project

import timeslicer.model.framework.RequestModel
import timeslicer.model.storage.Storage

case class GetProjectsRequestModel(storage:Storage) extends RequestModel {
  override def toString = {
    storage.toString()
  }
}          

