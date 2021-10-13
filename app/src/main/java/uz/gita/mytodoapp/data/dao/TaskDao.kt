package uz.gita.mytodoapp.data.dao

import androidx.room.*
import androidx.room.Dao
import uz.gita.mytodoapp.data.entity.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM TaskEntity WHERE TaskEntity.pagePos = :pos")
    fun getDataByPagePos(pos : Int) : List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data : TaskEntity)

    @Update
    fun update(data : TaskEntity)
    @Delete
    fun delete(data: TaskEntity)
}