package uz.gita.mytodoapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CheckListEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val title : String,
    val message  :String,
    val time : Long,
    val isPinned : Boolean
)