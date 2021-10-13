package uz.gita.mytodoapp.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.droidsonroids.gif.GifDrawable
import uz.gita.mytodoapp.R
import uz.gita.mytodoapp.data.AppDatabase
import uz.gita.mytodoapp.data.entity.TaskEntity
import uz.gita.mytodoapp.databinding.PageDoneBinding
import uz.gita.mytodoapp.ui.adapter.TaskAdapter
import uz.gita.mytodoapp.ui.dialog.EditTaskDialog
import uz.gita.mytodoapp.ui.dialog.EventDialog

class DonePage : Fragment(R.layout.page_done) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _viewBinding = PageDoneBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    private var _viewBinding: PageDoneBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val data = ArrayList<TaskEntity>()
    private val taskDao = AppDatabase.getDatabase().getTaskDao()
    private val adapter by lazy { TaskAdapter(data) }

    private var updateDoingPageListener: (() -> Unit)? = null
    private var listenerItem: ((TaskEntity) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val rv: RecyclerView = view.findViewById(R.id.doneList)
        val gifFromResource = GifDrawable(resources,R.drawable.todo)
        gifFromResource.start()
//        viewBinding.gif = view.findViewById(R.id.gif)

        if (data.isEmpty())
            viewBinding.gif.visibility = View.VISIBLE
        else viewBinding.gif.visibility = View.GONE
        loadData()
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(requireContext())
        adapter.setListenerItem {
            listenerItem?.invoke(it)
        }
        adapter.setListener { pos ->

            val bottomDialog = EventDialog()
            bottomDialog.setDeleteListener {
                taskDao.delete(data[pos])
                data.removeAt(pos)
                if (data.isEmpty())
                    viewBinding.gif.visibility = View.VISIBLE
                else viewBinding.gif.visibility = View.GONE
                adapter.notifyItemRemoved(pos)
            }
            bottomDialog.setEditListener {
                val editTaskDialog = EditTaskDialog()
                val bundle = Bundle()
                bundle.putSerializable("data", data[pos])
                editTaskDialog.arguments = bundle
                editTaskDialog.setListener {
                    data[pos] = it
                    taskDao.update(data[pos])
//                    adapter.notifyItemChanged(pos)
                    adapter.notifyDataSetChanged()
                }
                editTaskDialog.isCancelable = false
                editTaskDialog.show(childFragmentManager, "edit")
            }
            bottomDialog.show(childFragmentManager, "event")
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = ItemTouchHelper.LEFT
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    val taskEntity = data[viewHolder.adapterPosition]
                    taskEntity.pagePos = 1
                    taskDao.update(taskEntity)
                    data.removeAt(viewHolder.adapterPosition)
                    if (data.isEmpty())
                        viewBinding.gif.visibility = View.VISIBLE
                    else viewBinding.gif.visibility = View.GONE
                    adapter.notifyItemRemoved(viewHolder.adapterPosition)
                    updateDoingPageListener?.invoke()
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(rv)
    }

    fun loadData() {
        data.clear()
        data.addAll(taskDao.getDataByPagePos(2))
        adapter.notifyDataSetChanged()

    }

    override fun onResume() {
        super.onResume()
        if (data.isEmpty())
            viewBinding.gif.visibility = View.VISIBLE
        else viewBinding.gif.visibility = View.GONE
        adapter.notifyDataSetChanged()
    }

    fun setUpdateDoingPageListener(f: () -> Unit) {
        updateDoingPageListener = f
    }

    fun setListenerItem(f: (TaskEntity) -> Unit) {
        listenerItem = f
    }

}