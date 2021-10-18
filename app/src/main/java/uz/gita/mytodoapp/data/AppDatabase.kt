package uz.gita.mytodoapp.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.gita.mytodoapp.app.App
import uz.gita.mytodoapp.data.dao.AppointmentDao
import uz.gita.mytodoapp.data.dao.ContactDao
import uz.gita.mytodoapp.data.dao.TaskDao
import uz.gita.mytodoapp.data.entity.AppointmentEntity
import uz.gita.mytodoapp.data.entity.ContactEntity
import uz.gita.mytodoapp.data.entity.TaskEntity

@Database(entities = [TaskEntity::class,AppointmentEntity::class, ContactEntity::class],version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTaskDao() : TaskDao
    abstract fun getAppointmentDao() : AppointmentDao
    abstract fun getContactDao() : ContactDao

    companion object {
        private lateinit var instance : AppDatabase

        fun getDatabase() :AppDatabase {
            if (!::instance.isInitialized) {
                instance = Room.databaseBuilder(App.instance,AppDatabase::class.java,"Todo")
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }
}