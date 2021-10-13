package uz.gita.mytodoapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String,
    var phoneNumber: String,
) : Serializable