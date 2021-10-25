package uz.gita.mytodoappwithdagger.data.dao

import androidx.room.*
import uz.gita.mytodoappwithdagger.data.entity.AppointmentEntity

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