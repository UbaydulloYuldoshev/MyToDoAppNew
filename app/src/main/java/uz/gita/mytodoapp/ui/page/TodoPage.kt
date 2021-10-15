package uz.gita.mytodoapp.ui.page

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
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
import by.kirich1409.viewbindingdelegate.viewBinding
import pl.droidsonroids.gif.GifDrawable
import uz.gita.mytodoapp.AlarmReceiver
import uz.gita.mytodoapp.R
import uz.gita.mytodoapp.data.AppDatabase
import uz.gita.mytodoapp.data.entity.TaskEntity
import uz.gita.mytodoapp.databinding.FragmentMainBinding
import uz.gita.mytodoapp.databinding.PageToDoBinding
import uz.gita.mytodoapp.ui.adapter.TaskAdapter
import uz.gita.mytodoapp.ui.dialog.EditTaskDialog
import uz.gita.mytodoapp.ui.dialog.EventDialog

class TodoPage : Fragment(R.layout.page_to_do) {

    private val viewBinding by viewBinding(PageToDoBinding::bind)
    private val data = ArrayList<TaskEntity>()
    private val taskDao = AppDatabase.getDatabase().getTaskDao()
    private val adapter by lazy { TaskAdapter(data) }
    private var updateDoingPageListener: (() -> Unit)? = null
    private var listenerItem: ((TaskEntity) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createNotificationChannel()

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
                val editTaskDialog = EditTaskDialog(5)
                val bundle = Bundle()
                bundle.putSerializable("data",data[pos])
                editTaskDialog.arguments = bundle
                editTaskDialog.setListener {
                    data[pos] = it
                    taskDao.update(data[pos])
                    setNotification(data[pos],editTaskDialog.currentNotify)
                    adapter.notifyItemChanged(pos)
                }
                editTaskDialog.isCancelable = false
                editTaskDialog.show(childFragmentManager,"edit")
            }
            bottomDialog.show(childFragmentManager,"event")
        }

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
    private fun setNotification(entity: TaskEntity, time: Long) {

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)

        intent.putExtra("id", entity.id)
        intent.putExtra("title", entity.title)

        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "todoApplicationNotificationChannel"
            val description = "Channel for Todo App"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("todoChannelId", name, importance)
            channel.description = description
            val notificationManager: NotificationManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}