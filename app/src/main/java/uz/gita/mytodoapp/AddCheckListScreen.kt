package uz.gita.mytodoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import uz.gita.mytodoapp.data.entity.CheckListEntity
import uz.gita.mytodoapp.databinding.ScreenAddCheckListBinding
import uz.gita.mytodoapp.model.CheckData
import uz.gita.mytodoapp.ui.adapter.CheckListAdapter

class AddCheckListScreen : Fragment() {

    private var _binding: ScreenAddCheckListBinding? = null
    private val binding get() = _binding!!
    private val data = ArrayList<CheckData>()
    private val adapter =  CheckListAdapter(data)
    private var pos=0
    private val viewModel : AddCheckListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ScreenAddCheckListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.checkList.adapter=adapter
        binding.checkList.layoutManager= LinearLayoutManager(requireContext())
        firstLoad()

        binding.buttonAddCheckItem.setOnClickListener {
            data.add(CheckData(pos++,"",0))
            adapter.notifyItemInserted(data.size)
        }
        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.buttonSave.setOnClickListener {
            val builder = StringBuilder()
            for (element in data) {
                builder.append("${element.isChecked}@@@${element.text}###")
            }

            viewModel.addCheckList(
                CheckListEntity(
                    0,
                    binding.editTitle.text.toString(),
                    builder.toString(),
                    System.currentTimeMillis(),
                    true
                )
            )
            findNavController().popBackStack()
        }

        adapter.setCloseItemListener {
            data.removeAt(it)
            adapter.notifyItemRemoved(it)
        }
    }

    private fun firstLoad(){
        data.add(CheckData(pos++,"",0))
        adapter.notifyItemInserted(data.size)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


