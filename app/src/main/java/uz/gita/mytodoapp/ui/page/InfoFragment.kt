package uz.gita.mytodoapp.ui.page


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.gita.mytodoapp.R
import uz.gita.mytodoapp.data.entity.TaskEntity
import uz.gita.mytodoapp.databinding.FragmentInfoBinding
import uz.gita.mytodoapp.utils.toShortDate

class InfoFragment: Fragment() {
    private var _viewBinding : FragmentInfoBinding? = null
    private val viewBinding get() = _viewBinding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentInfoBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        _viewBinding = FragmentInfoBinding.bind(view)

        arguments?.let {
            val data = it.getSerializable("data") as TaskEntity
            viewBinding.textTaskName.text = "Task title: ${data.title}"
            viewBinding.textToDo.text = "Description: ${data.description}"
            viewBinding.textDeadlineTime.text = "${data.time}  ${data.timeAlarm}"
            if(data.pagePos == 0){
                viewBinding.view.setBackgroundColor(Color.parseColor("#A80505"))
                viewBinding.textProcessInfo.text = "You haven`t begun yet"}
            if(data.pagePos == 1){
                viewBinding.view.setBackgroundColor(Color.parseColor("#FF4C05"))
                viewBinding.textProcessInfo.text = "You are doing the task"}
            if(data.pagePos == 2){
                viewBinding.view.setBackgroundColor(Color.parseColor("#0B8F0B"))
                viewBinding.textProcessInfo.text = "You have done the task"}
        }
        viewBinding.onBackPress.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}