package timeslicer.model.session.impl

import timeslicer.model.session.Session
import timeslicer.model.session.SessionListenerService
import scala.collection.mutable.Seq

class SessionListenerServiceImpl extends SessionListenerService {

  val sessionCreatedListener: Seq[Session => Unit] = Seq()
  val sessionDestroyedListener: Seq[Session => Unit] = Seq()

  override def addSessionCreatedListener(f: Session => Unit) = {
    println("SessionListenerServiceImpl.addSessionCreatedListener:" + f)
    sessionCreatedListener :+ f
  }
  override def addSessionDestroyedListener(f: Session => Unit) = {
    println("SessionListenerServiceImpl.addSessionDestroyedListener:" + f)
    sessionDestroyedListener :+ f
  }
  override def create(session: Session) = sessionCreatedListener.foreach(f => {
    println("SessionListenerServiceImpl.create:" + session.id)
    f(session)
  })
  override def destroy(session: Session) = sessionDestroyedListener.foreach(f => {
    println("SessionListenerServiceImpl.destroy:" + session.id)
    f(session)
  })

}