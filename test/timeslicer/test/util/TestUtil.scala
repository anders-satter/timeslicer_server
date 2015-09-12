package timeslicer.test.util

import timeslicer.model.context.UseCaseContext
import timeslicer.model.framework.Result
import timeslicer.model.util.Util.EmptyUseCaseContext
import timeslicer.model.util.{ DateTime => dt }

object TestUtil {
  def logBeforeInteraction(caller: Any, r: timeslicer.model.framework.RequestModel, u: UseCaseContext) = ""
  def logAfterInteraction[S <: timeslicer.model.framework.ResponseModel](caller: Any, res: Result[S], u: UseCaseContext) = ""
  def emptyLog:String => Unit = (msg)=>{}
}