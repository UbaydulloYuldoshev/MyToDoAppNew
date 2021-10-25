package uz.gita.mytodoappwithdagger.data.dao

import androidx.room.*
import uz.gita.mytodoappwithdagger.data.entity.ContactEntity

@Dao
interface ContactDao {
    @Query("SELECT * FROM ContactEntity")
    fun getAllContact(): List<ContactEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: ContactEntity)

    @Update
    fun update(data: ContactEntity)

    @Delete
    fun delete(data: ContactEntity)
}