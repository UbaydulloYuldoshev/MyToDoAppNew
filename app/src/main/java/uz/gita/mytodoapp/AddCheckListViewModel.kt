package uz.gita.mytodoapp

import androidx.lifecycle.ViewModel
import uz.gita.mytodoapp.data.AppDatabase
import uz.gita.mytodoapp.data.entity.CheckListEntity

class AddCheckListViewModel : ViewModel() {

    private val database = AppDatabase.getDatabase().getCheckListDao()
    fun addCheckList(data: CheckListEntity) {
        database.insert(data)
    }
}
/***
 * private val database = AppDatabase.getDatabase().getCheckListDao()

fun addCheckList(data: CheckListEntity) {
database.insert(data)
}
 *
 * */