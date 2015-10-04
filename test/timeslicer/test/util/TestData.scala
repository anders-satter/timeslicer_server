package timeslicer.test.util

import timeslicer.model.timeslice.TimeSlice
import timeslicer.model.user.UserImpl

object TestData {
  
  val testdata = Seq(
    TimeSlice("2015-01-01", "10:00", "2015-01-01", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-02", "10:00", "2015-01-02", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-03", "10:00", "2015-01-03", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-04", "10:00", "2015-01-04", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-05", "10:00", "2015-01-05", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-06", "10:00", "2015-01-06", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-07", "10:00", "2015-01-07", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-08", "10:00", "2015-01-08", "10:30", "Prj1", "Act1", Option("")),
    TimeSlice("2015-01-09", "10:00", "2015-01-09", "10:30", "Prj1", "Act1", Option("")),

    TimeSlice("2015-01-01", "10:00", "2015-01-01", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-02", "10:00", "2015-01-02", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-03", "10:00", "2015-01-03", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-04", "10:00", "2015-01-04", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-05", "10:00", "2015-01-05", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-06", "10:00", "2015-01-06", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-07", "10:00", "2015-01-07", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-08", "10:00", "2015-01-08", "11:00", "Prj1", "Act2", Option("")),
    TimeSlice("2015-01-09", "10:00", "2015-01-09", "11:00", "Prj1", "Act2", Option("")),

    TimeSlice("2015-01-01", "10:00", "2015-01-01", "11:30", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-02", "10:00", "2015-01-02", "11:30", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-03", "10:00", "2015-01-03", "11:30", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-04", "10:00", "2015-01-04", "11:30", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-05", "10:00", "2015-01-05", "11:30", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-06", "10:00", "2015-01-06", "11:30", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-07", "10:00", "2015-01-07", "11:30", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-08", "10:00", "2015-01-08", "11:30", "Prj2", "Act1", Option("")),
    TimeSlice("2015-01-09", "10:00", "2015-01-09", "11:30", "Prj2", "Act1", Option("")),

    TimeSlice("2015-01-01", "10:00", "2015-01-01", "12:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-02", "10:00", "2015-01-02", "12:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-03", "10:00", "2015-01-03", "12:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-04", "10:00", "2015-01-04", "12:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-05", "10:00", "2015-01-05", "12:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-06", "10:00", "2015-01-06", "12:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-07", "10:00", "2015-01-07", "12:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-08", "10:00", "2015-01-08", "12:00", "Prj2", "Act2", Option("")),
    TimeSlice("2015-01-09", "10:00", "2015-01-09", "12:00", "Prj2", "Act2", Option("")))
  
    
    val user1 = new UserImpl
    user1.id = "111111111111"
    user1.firstName = "Liston"
    user1.lastName = "Priest"
   
    val user2 = new UserImpl
    user2.id = "222222222222"
    user2.firstName = "Jane"
    user2.lastName = "Doe"
    
    val user3 = new UserImpl    
    user3.id = "333333333333"
    user3.firstName = "Lynne"
    user3.lastName = "Charlton"

    val user4 = new UserImpl
    user4.id = "444444444444"
    user4.firstName = "Lars"
    user4.lastName = "Larsson-Lars"
    
    def testUsers = Some(Seq(user1, user2, user3, user4))
      
        
}