package uz.gita.mytodoapp.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.gita.mytodoapp.R
import uz.gita.mytodoapp.databinding.DialogEventBinding
import uz.gita.mytodoapp.databinding.PageDoneBinding


class EventDialog : BottomSheetDialogFragment() {
    private var _viewBinding : DialogEventBinding?= null
    private val viewBinging get() = _viewBinding!!
    private var editListener : (() -> Unit)? =null
    private var deleteListener : (() -> Unit)? =null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = DialogEventBinding.inflate(inflater, container, false)
        return viewBinging.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewBinging.edit.setOnClickListener {
            editListener?.invoke()
            dismiss()
        }
        viewBinging.delete.setOnClickListener {
            deleteListener?.invoke()
            dismiss()
        }
    }

    fun setEditListener(f : () -> Unit) {
        editListener = f
    }

    fun setDeleteListener(f : () -> Unit) {
        deleteListener = f
    }
}