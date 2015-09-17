package timeslicer.model.reporting

/*
 * A result structure that holds all projects within the reporting interval
 */
case class TotalResultStructure(val startday:String, val endday:String,val projects:Seq[SumProject], val totaltime:Double)
case class SumProject(val activities:Seq[SumActivity], val totaltime:Double, val percentOfTotal:Double)
case class SumActivity(val totaltime:Double, val percentOfTotal:Double)
