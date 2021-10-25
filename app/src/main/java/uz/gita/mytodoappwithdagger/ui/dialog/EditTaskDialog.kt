package uz.gita.mytodoappwithdagger.ui.dialog

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import uz.gita.mytodoappwithdagger.WorkManagerToDo
import uz.gita.mytodoappwithdagger.app.App
import uz.gita.mytodoappwithdagger.data.entity.TaskEntity
import uz.gita.mytodoappwithdagger.databinding.DialogEditTaskBinding
import java.util.*
import java.util.concurrent.TimeUnit

class EditTaskDialog : DialogFragment() {
    private var listener: ((TaskEntity) -> Unit)? = null
    private var _viewBinding: DialogEditTaskBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var calendar: Calendar
    private lateinit var data: TaskEntity
    var currentNotify = 0L
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _viewBinding = DialogEditTaskBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams

    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var pos = 0
        calendar = Calendar.getInstance()
        var time = ""
        var timeAlarm = ""
        var cYear = ""

        val date = Calendar.getInstance()
        arguments?.let {
            data = it.getSerializable("data") as TaskEntity
            pos = data.id
            viewBinding.taskTitle.editText?.setText(data.title)
            viewBinding.taskDescription.editText?.setText(data.description)
            viewBinding.addNoteAlarmTime.editText?.setText("${data.time}")
        }

        viewBinding.taskSelectedTime.setOnClickListener {
            DatePickerDialog(requireContext(), { _, year, month, day ->
                cYear = "$year/${month + 1}/$day"
                TimePickerDialog(requireContext(), { _, hour, minute ->
                    time = "$hour : $minute"
                    timeAlarm = "$hour : $minute"
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, month)
                        calendar.set(Calendar.DAY_OF_MONTH, day)
                        calendar.set(Calendar.HOUR_OF_DAY, hour + 12)
                        calendar.set(Calendar.MINUTE, minute)
                    }
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)

                    viewBinding.addNoteAlarmTime.editText?.setText("$cYear")
                    currentNotify = calendar.timeInMillis

                }, date.get(Calendar.HOUR), date.get(Calendar.MINUTE), true).show()
            },
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)).show()
        }

        viewBinding.buttonAdd.setOnClickListener {
            if (calendar.timeInMillis - Calendar.getInstance().timeInMillis > 0) {
                cancelRequest(data.idItem)
                val data = Data.Builder()
                data.putInt("id", 0)
                data.putString("title", "${viewBinding.taskTitle.editText?.text}")
                data.putString("description", "${viewBinding.taskDescription.editText?.text}")
                val uploadWorkerRequest: WorkRequest =
                    OneTimeWorkRequest.Builder(WorkManagerToDo::class.java)
                        .setInitialDelay(calendar.timeInMillis - Calendar.getInstance().timeInMillis,
                            TimeUnit.MILLISECONDS)
                        .setInputData(data.build()).build()
                WorkManager.getInstance(requireContext()).enqueue(uploadWorkerRequest)
                listener?.invoke(TaskEntity(pos,
                    viewBinding.taskTitle.editText?.text.toString(),
                    viewBinding.taskDescription.editText?.text.toString(),
                    0,
                    timeAlarm,
                    viewBinding.addNoteAlarmTime.editText?.text.toString(),
                    uploadWorkerRequest.stringId))
                dismiss()
            }
        }
        viewBinding.buttonNo.setOnClickListener {
            dismiss()
        }
    }
    private fun cancelRequest(id: String) {
        WorkManager.getInstance(App.instance)
            .cancelWorkById(UUID.fromString(id))
    }
    fun setListener(f: (TaskEntity) -> Unit) {
        listener = f
    }
}