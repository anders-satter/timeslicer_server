package timeslicer.model.usecase.userid

import timeslicer.model.context.UseCaseContext
import timeslicer.model.message.MessageBuilder
import timeslicer.model.storage.filestorage.FileStorage
import timeslicer.model.usecase.userid.exception.UserIdCouldNotBeGeneratedException
import timeslicer.model.util.StringIdGenerator
import timeslicer.model.framework.Interactor
import timeslicer.model.framework.Result
import scala.util.Failure
import timeslicer.model.util.Util.EmptyUseCaseContext
import timeslicer.model.util.Util.EmptyUseCaseContext

class CreateUserIdInteractor extends Interactor[CreateUserIdRequestModel, CreateUserIdResponseModel] {

  override def onExecute(request: CreateUserIdRequestModel,useCaseContext:UseCaseContext): Result[CreateUserIdResponseModel] = {

    val result = new Result[CreateUserIdResponseModel]
    
    /*
     * First load the users to make a list of userids so
     * I can make sure that the generated id is unique
     */        
    val currentUserIdList = storage.users().getOrElse(Seq()).map(u => u.id)
    
    
    /* Generate the id */
    var generatedValue = ""
    var valueCouldNotBeGenerated = false
    var breakCounter = 0
    do {      
    	generatedValue = StringIdGenerator.userId();
    	breakCounter = breakCounter + 1
    } while (currentUserIdList.contains(generatedValue) && breakCounter < 100)
      
    if (breakCounter >= 100 ){
      result.error = Failure(new UserIdCouldNotBeGeneratedException(          
           new MessageBuilder()
           .append("User id could not be created within the allowed number of tries (99)").toString))
    }  
    result.success = CreateUserIdResponseModel(generatedValue)
    result
  }

}