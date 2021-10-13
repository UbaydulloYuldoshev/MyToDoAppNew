package uz.gita.mytodoapp.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import uz.gita.mytodoapp.data.entity.AppointmentEntity
import uz.gita.mytodoapp.databinding.DialogEditAppTaskBinding

class EditTaskAppDialog : DialogFragment() {
    private var listener: ((AppointmentEntity) -> Unit)? = null
    private var _viewBinding: DialogEditAppTaskBinding? = null
    private val viewBinding get() = _viewBinding!!
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


        arguments?.let {
            val data = it.getSerializable("data") as AppointmentEntity
            pos = data.id
            viewBinding.taskTitle.editText?.setText(data.title)
            viewBinding.taskDescription.editText?.setText(data.place)
            viewBinding.taskSelectedTime.setText(data.time)
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