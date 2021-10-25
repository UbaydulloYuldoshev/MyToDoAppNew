package uz.gita.mytodoappwithdagger.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import uz.gita.mytodoappwithdagger.R
import uz.gita.mytodoappwithdagger.app.App
import uz.gita.mytodoappwithdagger.data.entity.ContactEntity
import uz.gita.mytodoappwithdagger.databinding.DialogAddContactBinding

class ContactDialog : DialogFragment(R.layout.dialog_add_contact) {

    private var listener: ((ContactEntity) -> Unit)? = null
    private var _viewBinding: DialogAddContactBinding? = null
    private val viewBinding get() = _viewBinding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _viewBinding = DialogAddContactBinding.inflate(inflater, container, false)
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

        viewBinding.addNoteBtn.setOnClickListener {
            if (viewBinding.addNoteTitle.editText?.text?.isNotEmpty() == true &&
                viewBinding.addNoteDescription.editText?.text?.isNotEmpty() == true
            ) {
                listener?.invoke(ContactEntity(0,
                    viewBinding.addNoteTitle.editText?.text.toString(),
                    viewBinding.addNoteDescription.editText?.text.toString()))
                dismiss()
            } else
                Toast.makeText(App.instance, "Please enter fields !", Toast.LENGTH_SHORT).show()

        }
        viewBinding.cancelNoteBtn.setOnClickListener {
            isCancelable = false
            dismiss()
        }
    }

    fun setDialogListener(f: (ContactEntity) -> Unit) {
        listener = f
    }
}