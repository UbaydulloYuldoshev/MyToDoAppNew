package uz.gita.mytodoapp.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import uz.gita.mytodoapp.data.entity.ContactEntity
import uz.gita.mytodoapp.databinding.DialogEditContactBinding
import java.util.*

class EditContactDialog : DialogFragment() {
    private var listener: ((ContactEntity) -> Unit)? = null
    private var _viewBinding: DialogEditContactBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var calendar: Calendar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _viewBinding = DialogEditContactBinding.inflate(inflater, container, false)
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

        calendar = Calendar.getInstance()
        var time = ""
        var timeAlarm = ""
        var cYear = ""

        val date = Calendar.getInstance()
        var pos = 0
        arguments?.let {
            val data = it.getSerializable("data") as ContactEntity
            pos = data.id
            viewBinding.taskTitle.editText?.setText(data.name)
            viewBinding.taskDescription.editText?.setText(data.phoneNumber)
        }

        viewBinding.buttonAdd.setOnClickListener {
            listener?.invoke(ContactEntity(pos,
                viewBinding.taskTitle.editText?.text.toString(),
                viewBinding.taskDescription.editText?.text.toString()))
            dismiss()
        }
        viewBinding.buttonNo.setOnClickListener {
            dismiss()
        }
    }

    fun setListener(f: (ContactEntity) -> Unit) {
        listener = f
    }
}