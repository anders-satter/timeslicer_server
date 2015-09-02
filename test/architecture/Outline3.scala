//package architecture
//
//import scala.util.Try
//import scala.util.Success
//import scala.util.Failure
//import timeslicer.model.context.UseCaseContext
//import timeslicer.model.api.Result
//import timeslicer.model.context.UseCaseContextImpl
//import timeslicer.model.api.ResponseModel
//
//object Outline3 {
//
//  /*
//   * DEFINITIONS
//   */
//  trait YInteractor[R <: timeslicer.model.api.RequestModel, Result] {
//    def doBefore(r: R) = {
//      println("doBefore")
//      println(r.logInfo)
//    }
//    def doAfter(res: Result) = {
//      println("doAfter")
//      println(res)
//    }
//    def execute(r: R, u: UseCaseContext,f:(R,UseCaseContext)=>Result) = {     
//      doBefore(r)
//      val res = f(r,u)
//      doAfter(res)
//    }
//  }
//
//  /*
//   * USE CASE IMPLEMENTATION
//   */
//  case class Req(value: String) extends timeslicer.model.api.RequestModel {
//    override def logInfo: String = {
//      value.toString
//    }
//  }
//  case class Resp(value: Int) extends timeslicer.model.api.ResponseModel {
//    override def logInfo: String = {
//      value.toString
//    }    
//  }
//  class InteractorTest extends YInteractor[Req, Result[Resp]] {}
//  
//  
//  val interactor = new InteractorTest
//  val useCaseContext: UseCaseContext = new UseCaseContextImpl
//
//  interactor.execute(Req("1"), useCaseContext, (r, u)=>{
//     println("In execute anonymous2")      
//      val result = new Result[Resp]
//      result.success = Option(Resp(r.value.toInt))   
//      result
//  })
//
//  def main(args: Array[String]): Unit = {}
//}