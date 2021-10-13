package uz.gita.mytodoapp.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import uz.gita.mytodoapp.R
import uz.gita.mytodoapp.data.entity.TaskEntity
import uz.gita.mytodoapp.databinding.DialogEditTaskBinding

class EditTaskDialog : DialogFragment() {
    private var listener: ((TaskEntity) -> Unit)? = null
    private var _viewBinding: DialogEditTaskBinding? = null
    private val viewBinding get() = _viewBinding!!
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var pos = 0


        arguments?.let {
            val data = it.getSerializable("data") as TaskEntity
            pos = data.id
            viewBinding.taskTitle.editText?.setText(data.title)
            viewBinding.taskDescription.editText?.setText(data.description)
            viewBinding.addNoteAlarmTime.editText?.setText(data.timeAlarm)
        }

        viewBinding.buttonAdd.setOnClickListener {
            listener?.invoke(TaskEntity(pos,
                viewBinding.taskTitle.editText?.text.toString(),
                viewBinding.taskDescription.editText?.text.toString(),
                0,
                viewBinding.taskSelectedTime.text.toString(),
                viewBinding.addNoteAlarmTime.editText?.text.toString()))
            dismiss()
        }
        viewBinding.buttonNo.setOnClickListener {
            dismiss()
        }
    }

    fun setListener(f: (TaskEntity) -> Unit) {
        listener = f
    }
}