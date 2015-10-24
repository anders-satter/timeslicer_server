package timeslicer.model.session

import timeslicer.model.user.User

trait Session {
  def id: String
  def user: User
}