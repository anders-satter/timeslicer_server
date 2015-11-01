package timeslicer.model.usecase.user

import timeslicer.model.framework.RequestModel
import timeslicer.model.user.User

case class CreateUserAccountRequestModel(userName: String,
                                         firstName: String,
                                         lastName: String,
                                         password:String,
                                         email: Option[String]) extends RequestModel {

  override def toString:String = {
    val builder = new StringBuilder();
    builder.append("UserName:")
    builder.append(userName)
    builder.append(" FirstName:")
    builder.append(firstName)
    builder.append(" LastName:")
    builder.append(lastName)
    builder.append(" Password:")
    builder.append("********")    
    builder.append(" Email:")
    builder.append(email.getOrElse("Not supplied"))
    builder.toString
  }
  
}