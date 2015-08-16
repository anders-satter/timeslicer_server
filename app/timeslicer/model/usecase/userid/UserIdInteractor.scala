package timeslicer.model.usecase.userid

import timeslicer.model.interactor.Interactor
import timeslicer.model.api.RequestModel
import timeslicer.model.context.UseCaseContext
import timeslicer.model.api.ResponseModel
import java.util.Base64
import scala.compat.Platform
import java.util.Calendar
import java.text.SimpleDateFormat

class UserIdInteractor extends Interactor {
  override def execute(request: RequestModel, useCaseContext: UseCaseContext): ResponseModel = {
    /*
     * Generate the userid
     */
    //val ns = System.nanoTime()
    //val ms = Platform.currentTime

    //67324384834468
    //1439728771250

    /*convert day to ms*/
    val ms = new java.util.Date().getTime()
    val cal = Calendar.getInstance
    val day = cal.get(Calendar.DAY_OF_YEAR)
    //println(day)

    val days = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(ms);
    //println(days)
    //cal.set()

//    val truncator = new SimpleDateFormat("yyyy-MM-dd:HH:mm")
//    val truncator = new SimpleDateFormat("yyyy-MM-dd")
//    val truncator = new SimpleDateFormat("yyyy-MM")
    val truncator = new SimpleDateFormat("yyyy")
    
    val truncatedTime = truncator.format(ms)
    println(truncatedTime.toString)

    val date = truncator.parse(truncatedTime)
    //println(date.getTime())
    
    //MjAxNS0wOA==

    /*get the day of ms*/
    //val strms = String.valueOf(ms)
    val encodedStr = Base64.getEncoder().encodeToString(truncatedTime.getBytes("utf-8"))

    val decodedStr = new String(Base64.getDecoder().decode(encodedStr), "utf-8")
    //    println(strms)
    println(encodedStr)
    println(decodedStr)

    return UserIdResponseModel("hej").asInstanceOf[ResponseModel]

  }
}