package timeslicer.model.usecase.user

import timeslicer.model.framework.ResponseModel
import timeslicer.model.user.User

case class CreateUserAccountResponseModel(user:User, isPasswordValid:Boolean) extends ResponseModel{
  override def toString:String = {
    val builder = new StringBuilder()
    builder.append("UserName:")
    builder.append(user.userName)
    builder.append(" Email:")
    builder.append(user.email.getOrElse(""))
    builder.append(" IsPasswordValid:")
    builder.append(isPasswordValid)
    builder.toString
  }
}