package timeslicer.models.log

import timeslicer.models.util.DateTime

case class Item(start:String, 
    end:String, 
    project:String, 
    activity:String, 
    comment:String, 
    dayValue:Long) {
	def duration:Long = {
	  DateTime.elapsedMinutes(start, end)
	}
}