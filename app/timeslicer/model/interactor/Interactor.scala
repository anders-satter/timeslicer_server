package timeslicer.model.interactor

import timeslicer.model.api.RequestModel
import timeslicer.model.api.ResponseModel

/*
 * Will execute on use case
 */
trait Interactor {
  def execute(request:RequestModel, response:ResponseModel)
}