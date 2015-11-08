package timeslicer.model.session

trait SessionListenerService {
  def addSessionCreatedListener(f: Session => Unit)
  def addSessionDestroyedListener(f: Session => Unit)
  def create(session:Session)
  def destroy(session:Session)  
}
