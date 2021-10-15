package uz.gita.mytodoapp.ui.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import uz.gita.mytodoapp.data.entity.AppointmentEntity
import uz.gita.mytodoapp.databinding.DialogEditAppTaskBinding
import java.util.*

class EditTaskAppDialog : DialogFragment() {
    private var listener: ((AppointmentEntity) -> Unit)? = null
    private var _viewBinding: DialogEditAppTaskBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var calendar: Calendar
    var currentNotify = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _viewBinding = DialogEditAppTaskBinding.inflate(inflater, container, false)
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
        var pos = 0
        calendar = Calendar.getInstance()

        var time = ""
        var timeAlarm = ""
        var cYear = ""

        val date = Calendar.getInstance()
        arguments?.let {
            val data = it.getSerializable("data") as AppointmentEntity
            pos = data.id
            viewBinding.taskTitle.editText?.setText(data.title)
            viewBinding.taskDescription.editText?.setText(data.place)
            viewBinding.taskSelectedTime.setText(data.time)
        }
        viewBinding.taskSelectedTime.setOnClickListener {
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
                    currentNotify = calendar.timeInMillis
                }, date.get(Calendar.HOUR), date.get(Calendar.MINUTE), true).show()
            },
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)).show()
        }

        viewBinding.buttonAdd.setOnClickListener {
            listener?.invoke(AppointmentEntity(pos,
                viewBinding.taskTitle.editText?.text.toString(),
                viewBinding.taskDescription.editText?.text.toString(),
                viewBinding.taskSelectedTime.text.toString(),
                ""))
            dismiss()
        }
        viewBinding.buttonNo.setOnClickListener {
            dismiss()
        }
    }

    fun setListener(f: (AppointmentEntity) -> Unit) {
        listener = f
    }
}