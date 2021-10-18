package uz.gita.mytodoapp.ui.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.work.*
import uz.gita.mytodoapp.R
import uz.gita.mytodoapp.WorkManagerToDo
import uz.gita.mytodoapp.app.App
import uz.gita.mytodoapp.data.entity.TaskEntity
import uz.gita.mytodoapp.databinding.DialogAddTaskBinding
import java.util.*
import java.util.concurrent.TimeUnit

class AddTaskDialog : DialogFragment(R.layout.dialog_add_task) {
    private var listener: ((TaskEntity, Long) -> Unit)? = null
    private var _viewBinding: DialogAddTaskBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var calendar: Calendar
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _viewBinding = DialogAddTaskBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar = Calendar.getInstance()

        var time = ""
        var timeAlarm = ""
        var cYear = ""

        val date = Calendar.getInstance()

        viewBinding.timeAlarm.setOnClickListener {
            DatePickerDialog(requireContext(), { _, year, month, day ->
                cYear = "$year/${month+1}/$day"
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

                    viewBinding.addNoteAlarmTime.editText?.setText("$cYear $timeAlarm")
                }, date.get(Calendar.HOUR), date.get(Calendar.MINUTE), true).show()
            },
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)).show()
        }
        viewBinding.addNoteBtn.setOnClickListener {
            if (viewBinding.addNoteTitle.editText?.text?.isNotEmpty() == true &&
                viewBinding.addNoteDescription.editText?.text?.isNotEmpty() == true
                && viewBinding.addNoteAlarmTime.editText?.text?.isNotEmpty() == true) {
                listener?.invoke(TaskEntity(0,
                    viewBinding.addNoteTitle.editText?.text.toString(),
                    viewBinding.addNoteDescription.editText?.text.toString(),
                    0,
                    time,
                    cYear),
                    calendar.timeInMillis)
//                createRequest(calendar.timeInMillis)
                isCancelable = false
                dismiss()
            } else
                Toast.makeText(App.instance, "Please enter fields !", Toast.LENGTH_SHORT).show()

        }
        viewBinding.cancelNoteBtn.setOnClickListener {
            isCancelable = false
            dismiss()
        }
    }
//    private fun createRequest(time:Long){
//        if( time>=0 ){
//
//            val data = Data.Builder()
//            data.putInt("id",0)
//            data.putString("title","${viewBinding.addNoteTitle.editText?.text}")
//            data.putString("title","${viewBinding.addNoteDescription.editText?.text}")
//
//            val uploadWorkerRequest: WorkRequest =
//                OneTimeWorkRequest.Builder(WorkManagerToDo::class.java)
//                    .setInitialDelay(time, TimeUnit.MILLISECONDS)
//                    .setInputData(data.build()).build()
//            WorkManager.getInstance(requireContext()).enqueue(uploadWorkerRequest)
//
//        }
//    }
    fun setDialogListener(f: (TaskEntity, Long) -> Unit) {
        listener = f
    }
}