package timeslicer.controller.user

import play.api.mvc.Controller
import play.api.mvc.Action
import timeslicer.model.usecase.user.GetUsersInteractor
import timeslicer.model.usecase.user.GetUsersRequestModel
import timeslicer.model.context.UseCaseContextImpl
import timeslicer.model.user.UserImpl
import timeslicer.model.usecase.user.GetUsersResponseModel
import timeslicer.model.util.JsonHelper

class UserController extends Controller {

  /**
   * return the users
   */
  def users = Action { request =>
    {
      val reqModel = GetUsersRequestModel()

      val interactor = new GetUsersInteractor
      val useCaseContext = new UseCaseContextImpl
      val user = new UserImpl
      user.firstName = "Anders"
      user.lastName = "Sätter"
      user.id = "111111111111"
      user.email = "anders.satter@users.com"

      useCaseContext.user = user
      
      val list = interactor
        .execute(reqModel, useCaseContext)
        .success
        .getOrElse(GetUsersResponseModel(Seq()))
        .userList

      if (list.nonEmpty) {
        
        
//            val userContainer = UsersContainer(jsonUserSeq)

    /*make a user case class to simplify persisting*/
//    val userWrites = Json.writes[JsonUser]
//    implicit val userSequenceWrites: Writes[Seq[JsonUser]] = Writes.seq(userWrites)
//    val containerWrites = Json.writes[UsersContainer]
//    val usersJson = Json.toJson(userContainer)(containerWrites)

        
        
        Ok(JsonHelper.jsonUserList(list))        
      } else {
        Ok("{}")
      }
    }
    
    
    
  }

}

