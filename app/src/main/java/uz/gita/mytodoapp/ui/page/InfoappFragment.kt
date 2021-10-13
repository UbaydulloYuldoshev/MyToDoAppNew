package uz.gita.mytodoapp.ui.page


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.gita.mytodoapp.data.entity.AppointmentEntity
import uz.gita.mytodoapp.databinding.FragmentInfoappBinding

class InfoappFragment: Fragment() {
    private var _viewBinding : FragmentInfoappBinding? = null
    private val viewBinding get() = _viewBinding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentInfoappBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        _viewBinding = FragmentInfoappBinding.bind(view)

        arguments?.let {
            val data = it.getSerializable("data") as AppointmentEntity
            viewBinding.textTaskName.text = "Meeting with: ${data.title}"
            viewBinding.textToDo.text = "Meeting place: ${data.place}"
            viewBinding.textDeadlineTime.text = "Meeting time: ${data.time}  ${data.deadlineHour}"
        }
        viewBinding.onBackPress.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}