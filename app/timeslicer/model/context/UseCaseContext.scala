package timeslicer.model.context

import timeslicer.model.user.User

/*
 * Superclass of context, which gains access to the user details and other
 * session related stuff
 */
trait UseCaseContext {
  def user:User 
}