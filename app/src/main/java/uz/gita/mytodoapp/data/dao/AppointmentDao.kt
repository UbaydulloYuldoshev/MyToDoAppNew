package uz.gita.mytodoapp.data.dao

import androidx.room.*
import uz.gita.mytodoapp.data.entity.AppointmentEntity
import uz.gita.mytodoapp.data.entity.TaskEntity

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM AppointmentEntity")
    fun getAllAppointments(): List<AppointmentEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: AppointmentEntity)

    @Update
    fun update(data: AppointmentEntity)

    @Delete
    fun delete(data: AppointmentEntity)
}