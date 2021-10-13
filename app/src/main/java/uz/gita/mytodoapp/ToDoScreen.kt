package uz.gita.mytodoapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.gita.mytodoapp.data.AppDatabase
import uz.gita.mytodoapp.data.entity.TaskEntity
import uz.gita.mytodoapp.databinding.ScreenTodoBinding
import java.util.*

class ToDoScreen : Fragment() {
    private var _viewBinding: ScreenTodoBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val taskDao = AppDatabase.getDatabase().getTaskDao()
    private lateinit var calendar: Calendar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _viewBinding = ScreenTodoBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createNotificationChannel()

        var time = ""
        var cYear = ""

        val date = Calendar.getInstance()
        var _hour = 0
        var _minute = 0
        viewBinding.addNoteAlarmTime.setOnClickListener {
            val timePicker = TimePickerDialog(requireContext(), { _, hour, minute ->
                time = "$hour : $minute"
                _hour = hour
                _minute = minute
                viewBinding.addNoteAlarmTime.text = "$hour.$minute"
//                viewBinding.addNoteAlarmTime.editText?.doOnTextChanged { text, _, _, _ ->
//
//                }
            }, date.get(Calendar.HOUR), date.get(Calendar.MINUTE), true)
            DatePickerDialog(requireContext(),
                { _, year, month, day ->
                    timePicker.show()
                    cYear = "$day.$month.$year"
                },
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)).show()
        }
        viewBinding.addNoteBtn.setOnClickListener {
            calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                calendar[Calendar.HOUR_OF_DAY] = _hour +12
                calendar[Calendar.MINUTE] = _minute
            }

            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            val taskEntity = TaskEntity(
                0,
                viewBinding.addNoteTitle.editText?.text.toString(),
                viewBinding.addNoteDescription.editText?.text.toString(),
                0,
                time,
                "")
            taskDao.insert(taskEntity)
            setNotification(taskEntity, calendar.timeInMillis)


        }
        viewBinding.cancelNoteBtn.setOnClickListener {
        }
    }

    private fun setNotification(entity: TaskEntity, time: Long) {

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)

        intent.putExtra("id", entity.id)
        intent.putExtra("title", entity.title)

        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "todoApplicationNotificationChannel"
            val description = "Channel for Todo App"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("todoChannelId", name, importance)
            channel.description = description
            val notificationManager: NotificationManager = requireActivity().getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
/**

@SuppressLint("SetTextI18n")
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
createNotificationChannel()
val date = Calendar.getInstance()
val calendar = Calendar.getInstance()
var timeAlarm = ""

viewBinding.addNoteAlarmTime.setOnClickListener {

DatePickerDialog(requireContext(), { _, year, month, day ->
val cYear = "$year $month $day"
TimePickerDialog(requireContext(), { _, hour, minute ->
timeAlarm = "$hour : $minute"
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
calendar.set(Calendar.YEAR, year)
calendar.set(Calendar.MONTH, month)
calendar.set(Calendar.DAY_OF_MONTH, day)
calendar.set(Calendar.HOUR_OF_DAY, hour+12)
calendar.set(Calendar.MINUTE, minute)
}
calendar.set(Calendar.SECOND, 0)
calendar.set(Calendar.MILLISECOND, 0)

viewBinding.addNoteAlarmTime.text = "$cYear $timeAlarm"
}, date.get(Calendar.HOUR), date.get(Calendar.MINUTE), true).show()
},
date.get(Calendar.YEAR),
date.get(Calendar.MONTH),
date.get(Calendar.DAY_OF_MONTH)).show()


}
viewBinding.addNoteBtn.setOnClickListener {
val taskEntity = TaskEntity(
0,
viewBinding.addNoteTitle.editText?.text.toString(),
viewBinding.addNoteDescription.editText?.text.toString(),
0,
timeAlarm,
time = System.currentTimeMillis())
taskDao.insert(taskEntity)
setNotification(taskEntity, calendar.timeInMillis)
Log.d("TTT", "${calendar.timeInMillis -taskEntity.time}")
Log.d("TTT", "${calendar.timeInMillis}  calendar")
Log.d("TTT", "${taskEntity.time}  entity")





*/