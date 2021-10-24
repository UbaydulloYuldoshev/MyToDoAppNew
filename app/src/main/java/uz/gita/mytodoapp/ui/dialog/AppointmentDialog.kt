package uz.gita.mytodoapp.ui.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import uz.gita.mytodoapp.R
import uz.gita.mytodoapp.WorkManagerToDo
import uz.gita.mytodoapp.app.App
import uz.gita.mytodoapp.data.entity.AppointmentEntity
import uz.gita.mytodoapp.databinding.DialogAppointmentBinding
import java.util.*
import java.util.concurrent.TimeUnit

class AppointmentDialog : DialogFragment(R.layout.dialog_appointment) {

    private var listener: ((AppointmentEntity, Long) -> Unit)? = null
    private var _viewBinding: DialogAppointmentBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var calendar2: Calendar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _viewBinding = DialogAppointmentBinding.inflate(inflater, container, false)
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

        calendar2 = Calendar.getInstance()

        var time = ""
        var timeHour = 0L
        var timeAlarm = ""
        var cYear = ""
        val date = Calendar.getInstance()
        viewBinding.timeAlarm.setOnClickListener {
            DatePickerDialog(requireContext(), { _, year, month, day ->
                cYear = "$year.${month+1}.$day"
                TimePickerDialog(requireContext(), { _, hour, minute ->
                    time = "$hour : $minute"
                    timeAlarm = "$hour : $minute"
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        calendar2.set(Calendar.YEAR, year)
                        calendar2.set(Calendar.MONTH, month)
                        calendar2.set(Calendar.DAY_OF_MONTH, day)
                        calendar2.set(Calendar.HOUR_OF_DAY, hour+12)
                        calendar2.set(Calendar.MINUTE, minute)
                    }
                    calendar2.set(Calendar.SECOND, 0)
                    calendar2.set(Calendar.MILLISECOND, 0)

                    viewBinding.addNoteAlarmTime.editText?.setText("$cYear $timeAlarm")
                }, date.get(Calendar.HOUR), date.get(Calendar.MINUTE), true).show()
            },
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)).show()
        }

        timeHour = calendar2.timeInMillis
        viewBinding.addNoteBtn.setOnClickListener {
            Log.d("TTTT",(calendar2.timeInMillis-Calendar.getInstance().timeInMillis).toString())
            if (calendar2.timeInMillis - Calendar.getInstance().timeInMillis > 0) {
                val data = Data.Builder()
                data.putInt("id", 0)
                data.putString("title", "${viewBinding.addNoteTitle.editText?.text}")
                data.putString("description", "${viewBinding.addNoteDescription.editText?.text}")
                val uploadWorkerRequest: WorkRequest =
                    OneTimeWorkRequest.Builder(WorkManagerToDo::class.java)
                        .setInitialDelay(calendar2.timeInMillis-Calendar.getInstance().timeInMillis, TimeUnit.MILLISECONDS)
                        .setInputData(data.build()).build()
                WorkManager.getInstance(requireContext()).enqueue(uploadWorkerRequest)
                if (viewBinding.addNoteTitle.editText?.text?.isNotEmpty() == true &&
                    viewBinding.addNoteDescription.editText?.text?.isNotEmpty() == true
                    && viewBinding.addNoteAlarmTime.editText?.text?.isNotEmpty() == true
                ) {
                    listener?.invoke(AppointmentEntity(0,
                        viewBinding.addNoteTitle.editText?.text.toString(),
                        viewBinding.addNoteDescription.editText?.text.toString(),
                        time,
                        cYear,
                        uploadWorkerRequest.id.toString()),
                        calendar2.timeInMillis)
                    isCancelable = false
                    dismiss()
                } else
                    Toast.makeText(App.instance, "Please enter fields !", Toast.LENGTH_SHORT).show()
            }
        }
        viewBinding.cancelNoteBtn.setOnClickListener {
            isCancelable = false
            dismiss()
        }
    }

    fun setDialogListener(f: (AppointmentEntity, Long) -> Unit) {
        listener = f
    }
}