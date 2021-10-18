package uz.gita.mytodoapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class AppointmentEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    var title:String,
    var place:String,
    var deadlineHour: String,
    var time:String,
    var idItem:String
):Serializable