package timeslicer.model.api

import timeslicer.model.context.UseCaseContext

/**
 * The Interactor is responsible for the main
 * execution of a use case.
 */
trait XInteractor[R <: RequestModel, Result ]  {
  def execute(request: R, useCaseContext: UseCaseContext): Result
}

//trait Before extends XInteractor[RequestModel, Result]
//add a performer layer

//create a performer container
case class Req(value:String) extends RequestModel
case class Resp(value:String) extends ResponseModel
case class Res() extends Result
class ConcrInteractorPerformer(interactor:XInteractor[Req, Res]) {
  def doBefore = {}
  def doAfter = {}
	def perform(r:Req, u:UseCaseContext) = {
    doBefore
    interactor.execute(r, u)
    doAfter
	}  
} 


trait SimpleInteractor[R <: RequestModel, Result ] {
  def execute(request: R, useCaseContext: UseCaseContext): Result
}

trait InteractionPerformer[R <: RequestModel, Result,I <:SimpleInteractor[R,Result] ] {
  def doBefore(r:R) = {}
  def doAfter(res:Result) = {}
  def perform(r:R, u:UseCaseContext,i:I) = {
    doBefore(r)
    val res = i.execute(r, u)
    doAfter(res)
  }      
}
//createInteractor







