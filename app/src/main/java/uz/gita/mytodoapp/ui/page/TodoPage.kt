package uz.gita.mytodoapp.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.droidsonroids.gif.GifDrawable
import uz.gita.mytodoapp.R
import uz.gita.mytodoapp.data.AppDatabase
import uz.gita.mytodoapp.data.entity.TaskEntity
import uz.gita.mytodoapp.databinding.FragmentMainBinding
import uz.gita.mytodoapp.databinding.PageToDoBinding
import uz.gita.mytodoapp.ui.adapter.TaskAdapter
import uz.gita.mytodoapp.ui.dialog.EditTaskDialog
import uz.gita.mytodoapp.ui.dialog.EventDialog

class TodoPage : Fragment(R.layout.page_to_do) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = PageToDoBinding.inflate(inflater, container, false)
        return viewBinding.root
    }
    private var _viewBinding: PageToDoBinding?=null
    private val viewBinding get() =_viewBinding!!
    private val data = ArrayList<TaskEntity>()
    private val taskDao = AppDatabase.getDatabase().getTaskDao()
    private val adapter by lazy { TaskAdapter(data) }
    private var updateDoingPageListener: (() -> Unit)? = null
    private var listenerItem: ((TaskEntity) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rv: RecyclerView = view.findViewById(R.id.todoList)
        val gifFromResource = GifDrawable(resources,R.drawable.todo)
        gifFromResource.start()

        loadData()
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(requireContext())

        adapter.setListenerItem {
            listenerItem?.invoke(it)
        }
        adapter.setListenerTime {

        }
        adapter.setListener { pos ->

            val bottomDialog = EventDialog()
            bottomDialog.setDeleteListener {
                taskDao.delete(data[pos])
                data.removeAt(pos)
                if( data.isEmpty() )
                    viewBinding.gif.visibility = View.VISIBLE
                else viewBinding.gif.visibility = View.GONE
                adapter.notifyItemRemoved(pos)
            }
            bottomDialog.setEditListener {
                val editTaskDialog = EditTaskDialog()
                val bundle = Bundle()
                bundle.putSerializable("data",data[pos])
                editTaskDialog.arguments = bundle
                editTaskDialog.setListener {
                    data[pos] = it
                    taskDao.update(data[pos])
                    adapter.notifyItemChanged(pos)
                }
                editTaskDialog.isCancelable = false
                editTaskDialog.show(childFragmentManager,"edit")
            }
            bottomDialog.show(childFragmentManager,"event")
        }
        /***
        adapter.setListener { pos ->
        val bottomDialog = EventDialog()
        bottomDialog.setDeleteListener {
        database.deleteContact(data[pos].id)
        data.removeAt(pos)
        adapter.notifyItemRemoved(pos)
        }
        bottomDialog.setEditListener {
        val editDialog = EditContactDialog()
        val bundle = Bundle()
        bundle.putSerializable("data",data[pos])
        editDialog.arguments = bundle
        editDialog.setListener {
        data[pos] = it
        database.editContact(it)
        adapter.notifyItemChanged(pos)
        }
        editDialog.show(supportFragmentManager,"edit")
        }
        bottomDialog.show(supportFragmentManager,"event")
        }
         */

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.RIGHT) {
                    val taskEntity = data[viewHolder.adapterPosition]
                    taskEntity.pagePos = 1
                    taskDao.update(taskEntity)
                    data.removeAt(viewHolder.adapterPosition)
                    if( data.isEmpty() )
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
        data.addAll(taskDao.getDataByPagePos(0))
        if( data.isEmpty() )
            viewBinding.gif.visibility = View.VISIBLE
        else viewBinding.gif.visibility = View.GONE
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        if( data.isEmpty() )
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