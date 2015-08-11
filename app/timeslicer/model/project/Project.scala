package timeslicer.model.project

//class Project() {
//  var _name: String = null
//  var _activityList: List[Activity] = null
//
//  def name = _name
//  def name_= (name: String) = _name = name
//
//  def activityList = _activityList
//  def activityList_= (activityList:List[Activity]):Unit = _activityList = activityList 
//}
case class Project(name: String) {
  var _activityList: List[Activity] = null
  def activityList = _activityList
  def activityList_=(activityList: List[Activity]): Unit = _activityList = activityList
}