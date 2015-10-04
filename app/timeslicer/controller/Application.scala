package timeslicer.controller

import play.api._
import play.api.mvc._

class Application extends Controller {

  def index = Action {
     //Ok(timeslicer.views.html.index("Your new application is ready."))    
	  Ok("timeslicer server is ready.\n")
  }
}
