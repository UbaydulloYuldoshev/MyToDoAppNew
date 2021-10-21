package uz.gita.mytodoapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val title : String,
    val description : String,
    var pagePos : Int = 0,
    var timeAlarm : String,
    var time: String,
    var idItem :String
): Serializable
