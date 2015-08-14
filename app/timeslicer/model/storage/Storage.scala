package timeslicer.model.storage

import timeslicer.model.project.Project
import timeslicer.model.context.UseCaseContext
import timeslicer.model.project.Activity
import timeslicer.model.timeslice.TimeSlice
import com.sun.j3d.utils.geometry.Project
import timeslicer.model.user.User
import timeslicer.model.storage.filestorage.FileStorage

/*
 * General Storage trait which will receive all calls from the
 * use cases, as to avoid dependencies directly to persistence from
 * the application business logic (use cases, the interactor
 * implementations)
 */
trait Storage {
  
  /**
   * All projects for the user supplied in UseCaseContext
   */
  def projects(useCaseContext:UseCaseContext):Option[Seq[Project]]
  
  /**
   * All activities for the project belonging to the user
   * supplied in UseCaseContext
   */
  def activities(project:Project, useCaseContext:UseCaseContext):Option[Seq[Activity]]
  
  /**
   * TimeSlices from start datetime to end datetime for user supplied in
   * UseCaseContext
   */  
  def timeslices(start:String, end:String, useCaseContext:UseCaseContext):Option[Seq[TimeSlice]]
  
  /**
   * All users in the application
   */
  def users():Option[Seq[User]]
  
  /**
   * Add a project for user in UseCaseContext
   */
  def addProject(project:Project, useCaseContext:UseCaseContext):Unit
  
  /**
   * Add an activity for user in UseCaseContext
   */
  def addActivity(project:Project,activity:Activity, useCaseContext:UseCaseContext):Unit
  
  /**
   * Add a timeslice for user in UseCaseContext
   */
  def addTimeSlice(timeslice:TimeSlice, useCaseContext:UseCaseContext):Unit  
  /**
   * Add a timeslice for user in UseCaseContext
   */
  def addUser(user:User, useCaseContext:UseCaseContext):Unit  
}
