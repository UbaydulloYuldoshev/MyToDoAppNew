package uz.gita.mytodoapp.ui.screens
//
//import android.graphics.Color
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import androidx.appcompat.app.AppCompatDelegate
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.navigation.fragment.findNavController
//import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
//import com.github.dhaval2404.colorpicker.model.ColorShape
//import com.github.dhaval2404.colorpicker.model.ColorSwatch
//import uz.gita.noteapplication.R
//import uz.gita.noteapplication.app.App
//import uz.gita.noteapplication.data.entity.NoteEntity
//import uz.gita.noteapplication.databinding.ScreenAddNoteBinding
//import uz.gita.noteapplication.ui.viewmodel.AddNoteViewModel
//
//class AddNoteScreen : Fragment(), AdapterView.OnItemSelectedListener {
//    private var _binding: ScreenAddNoteBinding? = null
//    private val binding get() = _binding!!
//    private val viewModel: AddNoteViewModel by viewModels()
//    private var isPinned = false
//    private var indexOne = 0;
//    private var indexTwo = 0;
//    private var indexThree = 0;
//    private var indexFour = 0;
//    private var indexFive = 0;
//    private var indexSeven = 0;
//    private var colorIndex = "#C781D3"
//    private var state = true
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View? {
//        _binding = ScreenAddNoteBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//
//
//        binding.buttonBack.setOnClickListener {
//            findNavController().popBackStack()
//        }
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//
//
//        binding.editor.setUnderline()
//        binding.editor.setSubscript()
//        binding.editor.setPadding(10, 10, 10, 10)
//        binding.editor.setPlaceholder("Insert text here")
//
//        binding.editor.setBackgroundColor(ContextCompat.getColor(requireContext(),
//            R.color.white))
//        binding.appBar.setBackgroundColor(Color.parseColor(colorIndex))
//
//        binding.constraint.setBackgroundColor(Color.parseColor(colorIndex))
//        binding.editor.setBackgroundColor(Color.parseColor(colorIndex))
//
//
//        binding.buttonColorPicker.setOnClickListener {
//            if (indexFive == 0) {
//                indexFive = 1
//                binding.buttonColorPicker.setBackgroundColor(ContextCompat.getColor(App.instance,
//                    R.color.white))
//                MaterialColorPickerDialog
//                    .Builder(requireContext())                            // Pass Activity Instance
//                    .setTitle("Pick Theme")                // Default "Choose Color"
//                    .setColorShape(ColorShape.SQAURE)    // Default ColorShape.CIRCLE
//                    .setColorSwatch(ColorSwatch._300)    // Default ColorSwatch._500
//                    .setDefaultColor(R.color.green_100)        // Pass Default Color
//                    .setColorListener { color, colorHex ->
//                        colorIndex = colorHex
//                        binding.appBar.setBackgroundColor(Color.parseColor(colorHex))
//                        binding.editor.setBackgroundColor(Color.parseColor(colorHex))
//                        binding.constraint.setBackgroundColor(Color.parseColor(colorHex))
//                    }
//                    .show()
//
//            } else {
//                indexFive = 0
////                binding.editor.setBold()
//                binding.buttonColorPicker.setBackgroundColor(ContextCompat.getColor(App.instance,
//                    R.color.white))
//
//            }
//        }
//        ArrayAdapter.createFromResource(requireContext(),
//            R.array.number_array,
//            android.R.layout.simple_spinner_item).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            binding.spinnerEdit.adapter = adapter
//        }
//        binding.spinnerEdit.onItemSelectedListener = this
//
//        binding.buttonBackgroundText.setOnClickListener {
//            if (indexSeven == 0) {
//                indexSeven = 1
//                binding.buttonBackgroundText.setBackgroundColor(ContextCompat.getColor(App.instance,
//                    R.color.colorBackground))
//
//                MaterialColorPickerDialog
//                    .Builder(requireContext())                            // Pass Activity Instance
//                    .setTitle("Pick Theme")                // Default "Choose Color"
//                    .setColorShape(ColorShape.SQAURE)    // Default ColorShape.CIRCLE
//                    .setColorSwatch(ColorSwatch._300)    // Default ColorSwatch._500
//                    .setDefaultColor(R.color.green_100)        // Pass Default Color
//                    .setColorListener { color, colorHex ->
//                        binding.editor.setTextColor(Color.parseColor(colorHex))
//                    }
//                    .show()
//            } else {
//                indexSeven = 0
//                binding.buttonBackgroundText.setBackgroundColor(ContextCompat.getColor(App.instance,
//                    R.color.white))
//                binding.editor.setTextColor(Color.parseColor("#000000"))
//            }
//        }
//        binding.buttonTextStyleBold.setOnClickListener {
//            if (indexOne == 0) {
//                indexOne = 1
//                binding.buttonTextStyleBold.setBackgroundColor(ContextCompat.getColor(App.instance,
//                    R.color.colorBackground))
//                binding.editor.setBold()
//            } else {
//                indexOne = 0
//                binding.editor.setBold()
//                binding.buttonTextStyleBold.setBackgroundColor(ContextCompat.getColor(App.instance,
//                    R.color.white))
//
//            }
//        }
//
//        binding.buttonTextStyleItalic.setOnClickListener {
//            if (indexTwo == 0) {
//                indexTwo = 1
//                binding.buttonTextStyleItalic.setBackgroundColor(ContextCompat.getColor(App.instance,
//                    R.color.colorBackground))
//                binding.editor.setItalic()
//            } else {
//                indexTwo = 0
//                binding.editor.setItalic()
//                binding.buttonTextStyleItalic.setBackgroundColor(ContextCompat.getColor(App.instance,
//                    R.color.white))
//
//            }
//        }
//
//        binding.buttonTextStyleunderline.setOnClickListener {
//            if (indexThree == 0) {
//                indexThree = 1
//                binding.buttonTextStyleunderline.setBackgroundColor(ContextCompat.getColor(App.instance,
//                    R.color.colorBackground))
//                binding.editor.setUnderline()
//            } else {
//                indexThree = 0
//                binding.editor.setUnderline()
//                binding.buttonTextStyleunderline.setBackgroundColor(ContextCompat.getColor(App.instance,
//                    R.color.white))
//
//            }
//        }
//
//        binding.buttonPin.setOnClickListener {
//            if (indexFour == 0) {
//                indexFour = 1
//                binding.buttonPin.setBackgroundColor(ContextCompat.getColor(App.instance,
//                    R.color.colorBackground))
//                isPinned = !isPinned
//            } else {
//                indexFour = 0
//                isPinned = !isPinned
//                binding.buttonPin.setBackgroundColor(ContextCompat.getColor(App.instance,
//                    R.color.white))
//
//            }
//        }
//        binding.buttonSave.setOnClickListener {
//            state = false
//            if (binding.editor.html.isNullOrEmpty()) {
//                findNavController().popBackStack()
//                return@setOnClickListener
//            }
//            if (binding.editTitle.text.isNullOrEmpty()) {
//                binding.editTitle.setText("Title")
//            }
//            viewModel.addNote(
//                NoteEntity(
//                    0,
//                    binding.editTitle.text.toString(),
//                    binding.editor.html,
//                    System.currentTimeMillis(),
//                    colorIndex,
//                    0,
//                    isPinned,
//                ))
//            findNavController().popBackStack()
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
//        val item = parent?.getItemAtPosition(pos).toString()
//        when (pos) {
//            0 -> {
//                binding.textSize.text = item
//                binding.editor.setFontSize(7)
//            }
//            1 -> {
//                binding.textSize.text = item
//                binding.editor.setFontSize(5)
//            }
//            2 -> {
//                binding.textSize.text = item
//                binding.editor.setFontSize(4)
//            }
//            3 -> {
//                binding.textSize.text = item
//                binding.editor.setFontSize(2)
//            }
//        }
//
//    }
//
//    override fun onNothingSelected(parent: AdapterView<*>?) {
//    }
//
//    override fun onPause() {
//        super.onPause()
//        if (state) {
//            if (binding.editor.html.isNullOrEmpty()) {
//                return
//            }
//            if (binding.editTitle.text.isNullOrEmpty()) {
//                binding.editTitle.setText("Title")
//            }
//            viewModel.addNote(
//                NoteEntity(
//                    0,
//                    binding.editTitle.text.toString(),
//                    binding.editor.html,
//                    System.currentTimeMillis(),
//                    colorIndex,
//                    0,
//                    isPinned,
//                ))
//        }
//    }
//
//}
