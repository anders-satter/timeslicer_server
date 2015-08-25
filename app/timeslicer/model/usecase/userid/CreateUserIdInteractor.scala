package timeslicer.model.usecase.userid

import timeslicer.model.api.RequestModel
import timeslicer.model.api.ResponseModel
import timeslicer.model.context.UseCaseContext
import timeslicer.model.interactor.Interactor
import timeslicer.model.message.MessageBuilder
import timeslicer.model.storage.filestorage.FileStorage
import timeslicer.model.usecase.userid.exception.UserIdCouldNotBeGeneratedException
import timeslicer.model.util.UserIdGenerator

class CreateUserIdInteractor extends Interactor[CreateUserIdRequestModel, CreateUserIdResponseModel] {

  override def execute(request: CreateUserIdRequestModel, useCaseContext: UseCaseContext): CreateUserIdResponseModel = {

    /*
     * First load the users to make a list of userids so
     * I can make sure that the generated id is unique
     */
    val currentUserIdList = {
      FileStorage().users() match {
        case Some(users) => {
          users.map(u => u.id)
        }
        case None => List()
      }
    }
    
    /* Generate the id */
    var generatedValue = ""
    var valueCouldNotBeGenerated = false
    var breakCounter = 0
    do {      
    	generatedValue = UserIdGenerator.generate;
    	breakCounter = breakCounter + 1
    } while (currentUserIdList.contains(generatedValue) && breakCounter < 100)
    if (breakCounter >= 100 ){
      throw new UserIdCouldNotBeGeneratedException(          
           new MessageBuilder()
           .append("User id could not be created within the allowed number of tries (99)").toString)
    }  
    return CreateUserIdResponseModel(generatedValue)
  }

  //  /**
  //   * Just some tests in this function below
  //   */
  //  private def uniqIdGenerationTest = {
  //    val ms = new java.util.Date().getTime()
  //    val cal = Calendar.getInstance
  //    val day = cal.get(Calendar.DAY_OF_YEAR)
  //    val days = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(ms);
  //    val truncator = new SimpleDateFormat("yyyy")
  //
  //    val truncatedTime = truncator.format(ms)
  //    println(truncatedTime.toString)
  //
  //    val date = truncator.parse(truncatedTime)
  //    //println(date.getTime())
  //
  //    //MjAxNS0wOA==
  //
  //    /*get the day of ms*/
  //    //val strms = String.valueOf(ms)
  //    val encodedStr = Base64.getEncoder().encodeToString(truncatedTime.getBytes("utf-8"))
  //
  //    val decodedStr = new String(Base64.getDecoder().decode(encodedStr), "utf-8")
  //    //    println(strms)
  //    println(encodedStr)
  //    println(decodedStr)
  //
  //    println(java.util.UUID.randomUUID())
  //    println(new java.rmi.server.UID())
  //
  //  }

}