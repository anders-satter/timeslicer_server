package timeslicer.model.framework

import timeslicer.model.util.{ Util => u, DateTime => dt }
import timeslicer.model.context.UseCaseContext
import timeslicer.model.util.Util.EmptyUseCaseContext
import timeslicer.model.util.Util.EmptyUseCaseContext

/**
 * Implementations for interaction logging functions
 */
object InteractionLogStringBuilder {
  val PIPE = "|"
  def time(nowMs: Long): dt.Now = {
    dt.Now(dt.now, dt.getDayValueInStr, dt.fullTimePart)
  }

  def logBeforeInteraction(caller: Any, 
      r: timeslicer.model.framework.RequestModel, 
      u: UseCaseContext, 
      interactionId:String) = {
    val buf = new StringBuilder
    val t = time(dt.now)
    buf.append(t.day)
    buf.append(PIPE)
    buf.append(t.time)
    buf.append(PIPE)
    buf.append("PRE")    
    buf.append(PIPE)
    buf.append(u.sessionId)
    buf.append(PIPE)
    buf.append(interactionId)
    
    buf.append(PIPE)

    /*
     * Ugly, but we don't want logging to fail
     */
    if (u != null && u.user != null) {
      buf.append(u.user.id)
    } else {
      buf.append(new EmptyUseCaseContext().user.id)
    }
    buf.append(PIPE)
    buf.append(caller.getClass.getSimpleName)
    buf.append(PIPE)
    buf.append(r.getClass.getSimpleName)
    buf.append(PIPE)
    buf.append(r.toString)
    buf.toString
  }

  def logAtError(caller: Any, u: UseCaseContext, e:Throwable, id:String): String = {
    val buf = new StringBuilder
    val t = time(dt.now)
    buf.append(PIPE)
    buf.append(t.time)
    buf.append(PIPE)
    if (u != null && u.user != null) {
      buf.append(u.user.id)
    } else {
      buf.append(new EmptyUseCaseContext().user.id)
    }
    buf.append(PIPE)
    buf.append("ERROR")    
    buf.append(PIPE)
    buf.append(id)
    buf.append(PIPE)
    buf.append(caller.getClass().getSimpleName)
    buf.append(PIPE)
    buf.append(e.getMessage)
    buf.append(PIPE)
    buf.append(e.getStackTrace.mkString("\n"))
    buf.toString
  }

  def logAfterInteraction[S <: timeslicer.model.framework.ResponseModel](caller: Any, res: Result[S], u: UseCaseContext, interactionId:String) = {
    val buf = new StringBuilder
    val t = time(dt.now)
    buf.append(t.day)
    buf.append(PIPE)
    buf.append(t.time)
    buf.append(PIPE)
    buf.append("PST")
    buf.append(PIPE)
    buf.append(u.sessionId)
    buf.append(PIPE)
    buf.append(interactionId)
    buf.append(PIPE)
    /*
     * Ugly, but we don't want logging to fail
     */
    if (u != null && u.user != null) {
      buf.append(u.user.id)
    } else {
      buf.append(new EmptyUseCaseContext().user.id)
    }
    buf.append(PIPE)
    buf.append(caller.getClass.getSimpleName)

    res.success.map(s => {
      buf.append(PIPE)
      buf.append("SUCCESS")
      buf.append(PIPE)
      buf.append(s.getClass.getSimpleName)
      buf.append(PIPE)
      buf.append(s.toString)
      buf.toString
    })

    res.error.map(c => {
      buf.append(PIPE)
      buf.append("ERROR")
      buf.append(PIPE)
      /*
         * map, foreach doesn't find the error, we have to use
         * get, but if we come here there *must* be an error :)
         * get cannot be used, because this will throw the exception!
         * we just want to read it and the stack trace
         * so we use Failure.failed = Success(Throwable)
         */
      //buf.append(t.failed.get.getStackTrace.mkString("\n"))
      //buf.append(t.failure.failed.get.getStackTrace.mkString)

      buf.append(c.id)
      buf.append(PIPE)
      buf.append(c.failure)
      buf.append(PIPE)
      buf.append(c.failure.get.getStackTrace.mkString("\n"))
    })
    res.failure.map(c => {
    	buf.append(PIPE)
    	buf.append("FAILURE")
    	buf.append(PIPE)
    	/*
    	 * map, foreach doesn't find the error, we have to use
    	 * get, but if we come here there *must* be an error :)
    	 * get cannot be used, because this will throw the exception!
    	 * we just want to read it and the stack trace
    	 * so we use Failure.failed = Success(Throwable)
    	 */
    	//buf.append(t.failed.get.getStackTrace.mkString("\n"))
    	//buf.append(t.failure.failed.get.getStackTrace.mkString)
    	
    	buf.append(c.message)
    	buf.append(PIPE)
    })
    buf.toString
  }
}

