package uz.gita.mytodoapp.ui.screens


//
//class EditNoteScreen : Fragment(), AdapterView.OnItemSelectedListener {
//    private var _binding: ScreenEditNoteBinding? = null
//    private val binding get() = _binding!!
//    private val viewModel: EditNoteViewModel by viewModels()
//    private var isPinned = false
//    private var indexOne = 0
//    private var indexTwo = 0
//    private var indexThree = 0
//    private var indexFour = 0
//    private var indexFive = 0
//    private val database = AppDatabase.getDatabase()
//    private var indexSeven = 0
//    private var color = "#FFFFFF"
//    private lateinit var data : NoteEntity
//    private var state = true
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View? {
//        _binding = ScreenEditNoteBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//
//        binding.buttonBack.setOnClickListener {
//            findNavController().popBackStack()
//        }
//        binding.buttonDelete.setOnClickListener {
//            database.getNoteDao().delete(data)
//            findNavController().navigateUp()
//        }
//
//
//        val clipboard = getSystemService(App.instance,ClipboardManager::class.java) as ClipboardManager
//        binding.buttonCopy.setOnClickListener {
//            val textToCopy = binding.editor.html.toString()
//            val clip = ClipData.newHtmlText("RANDOM UUID",textToCopy,textToCopy)
//            clipboard.setPrimaryClip(clip)
//            Toast.makeText(App.instance,"Copied!",Toast.LENGTH_SHORT).show()
//        }
//        arguments?.let {
//            data = it.getSerializable("data") as NoteEntity
//            binding.editTitle.setText(data.title)
//            binding.editor.html = data.message
//            color = data.color
//        }
//
//        binding.appBar.setBackgroundColor(Color.parseColor(data.color))
//        binding.constraint.setBackgroundColor(Color.parseColor(data.color))
//        binding.editor.setBackgroundColor(Color.parseColor(data.color))
//        binding.editor.setPadding(10, 20, 10, 10)
//        binding.editor.setPlaceholder("Insert text here")
//
//        ArrayAdapter.createFromResource(requireContext(), R.array.number_array,android.R.layout.simple_spinner_item).also {
//                adapter->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            binding.spinnerEdit.adapter = adapter
//        }
//        binding.spinnerEdit.onItemSelectedListener = this
//
//        binding.editor.setUnderline()
//        binding.buttonShare.setOnClickListener {
//            try {
//                val i = Intent(Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_SUBJECT, "");
//                val sAux ="${data.message}"; // here define package name of you app
//                i.putExtra(Intent.EXTRA_TEXT, sAux);
//                startActivity(Intent.createChooser(i, "choose one"));
//            } catch ( e:Exception) {
//               print(e)
//            }
//        }
//
//        binding.buttonColorPicker.setOnClickListener {
//            if (indexFive == 0) {
//                indexFive = 1
//                binding.buttonColorPicker.setBackgroundColor(ContextCompat.getColor(App.instance,
//                    R.color.colorBackground))
//                MaterialColorPickerDialog
//                    .Builder(requireContext())
//                    .setTitle("Pick Theme")
//                    .setColorShape(ColorShape.SQAURE)
//                    .setColorSwatch(ColorSwatch._300)
//                    .setDefaultColor(R.color.green_100)
//                    .setColorListener { _, colorHex ->
//                        color = colorHex
//                        binding.appBar.setBackgroundColor(Color.parseColor(colorHex))
//                        binding.editor.setBackgroundColor(Color.parseColor(colorHex))
//                        binding.constraint.setBackgroundColor(Color.parseColor(colorHex))
//                    }
//                    .show()
//
//            } else {
//                indexFive = 0
//                binding.buttonColorPicker.setBackgroundColor(ContextCompat.getColor(App.instance,
//                    R.color.white))
//            }
//        }
//
//        binding.buttonBackgroundText.setOnClickListener {
//            if (indexSeven == 0) {
//                indexSeven = 1
//                binding.buttonBackgroundText.setBackgroundColor(ContextCompat.getColor(App.instance,
//                    R.color.colorBackground))
//
//                 MaterialColorPickerDialog
//                    .Builder(requireContext())
//                    .setTitle("Pick Theme")
//                    .setColorShape(ColorShape.SQAURE)
//                    .setColorSwatch(ColorSwatch._300)
//                    .setDefaultColor(R.color.green_100)
//                    .setColorListener { _, colorHex ->
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
//            if(binding.editor.html.isNullOrEmpty()) {
//                return@setOnClickListener
//            }
//            if( binding.editTitle.text.isNullOrEmpty()){
//                binding.editTitle.setText("Title")
//            }
//            viewModel.update(
//                NoteEntity(
//                    data.id,
//                    binding.editTitle.text.toString(),
//                    binding.editor.html,
//                    System.currentTimeMillis(),
//                    color,
//                    data.state,
//                    isPinned,
//                ))
//            findNavController().navigateUp()
//        }
//    }
//
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    override fun onItemSelected(parent: AdapterView<*>?, view : View?, pos: Int, id : Long) {
//        val item = parent?.getItemAtPosition(pos).toString()
//        when(pos){
//            0->{
//                binding.textSize.text = item
//                binding.editor.setFontSize(7)
//            }
//            1->{
//                binding.textSize.text = item
//                binding.editor.setFontSize(6)
//            }
//            2->{
//                binding.textSize.text = item
//                binding.editor.setFontSize(5)
//            }
//            3->{
//                binding.textSize.text = item
//                binding.editor.setFontSize(4)
//            }
//            4->{
//                binding.textSize.text = item
//                binding.editor.setFontSize(3)
//            }
//            5->{
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
//        if( state ){
//        if(binding.editor.html.isNullOrEmpty()) {
//            return
//        }
//        if( binding.editTitle.text.isNullOrEmpty()){
//            binding.editTitle.setText("Title")
//        }
//        viewModel.update(
//            NoteEntity(
//                data.id,
//                binding.editTitle.text.toString(),
//                binding.editor.html,
//                System.currentTimeMillis(),
//                color,
//                data.state,
//                isPinned,
//            ))}
//    }
//}