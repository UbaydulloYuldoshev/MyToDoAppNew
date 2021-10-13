package uz.gita.mytodoapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import uz.gita.mytodoapp.data.entity.CheckListEntity

@Dao
interface CheckListDao : BaseDao<CheckListEntity> {

    @Query("SELECT * FROM CheckListEntity")
    fun getAllNotes(): List<CheckListEntity>

}