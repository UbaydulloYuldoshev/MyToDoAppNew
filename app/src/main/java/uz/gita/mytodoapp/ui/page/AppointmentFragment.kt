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
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import uz.gita.mytodoapp.AlarmReceiver
import uz.gita.mytodoapp.R
import uz.gita.mytodoapp.app.App
import uz.gita.mytodoapp.data.AppDatabase
import uz.gita.mytodoapp.data.entity.AppointmentEntity
import uz.gita.mytodoapp.databinding.FragmentAppointmentBinding
import uz.gita.mytodoapp.ui.adapter.AppointmentAdapter
import uz.gita.mytodoapp.ui.dialog.AppointmentDialog
import uz.gita.mytodoapp.ui.dialog.EditTaskAppDialog
import uz.gita.mytodoapp.ui.dialog.EventAppDialog

class AppointmentFragment : Fragment(R.layout.fragment_appointment) {
    private var _viewBinding: FragmentAppointmentBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val data = ArrayList<AppointmentEntity>()
    private val taskDao = AppDatabase.getDatabase().getAppointmentDao()
    private var listenerItem: ((AppointmentEntity) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _viewBinding = FragmentAppointmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadData()
        _viewBinding = FragmentAppointmentBinding.bind(view)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        createNotificationChannel()
        if (data.isEmpty())
            viewBinding.appointment.visibility = View.VISIBLE
        else
            viewBinding.appointment.visibility = View.INVISIBLE

        val adapter = AppointmentAdapter(data)
        viewBinding.list.adapter = adapter
        viewBinding.list.layoutManager = LinearLayoutManager(App.instance)


        adapter.setListener { pos ->

            val bottomDialog = EventAppDialog()
            bottomDialog.setDeleteListener {
                taskDao.delete(data[pos])
                data.removeAt(pos)
                if (data.isEmpty())
                    viewBinding.appointment.visibility = View.VISIBLE
                else viewBinding.appointment.visibility = View.GONE
                adapter.notifyItemRemoved(pos)
            }
            bottomDialog.setEditListener {
                val editTaskDialog = EditTaskAppDialog()
                val bundle = Bundle()
                bundle.putSerializable("data", data[pos])
                editTaskDialog.arguments = bundle
                editTaskDialog.setListener {
                    data[pos] = it
                    taskDao.update(data[pos])
                    adapter.notifyItemChanged(pos)
                }
                editTaskDialog.isCancelable = false
                editTaskDialog.show(childFragmentManager, "edit")
            }
            bottomDialog.show(childFragmentManager, "event")
        }
        adapter.setListenerItemTime {
            val bundle = Bundle()
            bundle.putSerializable("data", data[it])
            findNavController().navigate(R.id.action_appointmentFragment_to_infoappFragment, bundle)
        }

        viewBinding.buttonAdd.setOnClickListener {
            val addAppointment = AppointmentDialog()
            addAppointment.setDialogListener { entity, time ->
                taskDao.insert(entity)
                data.add(entity)
                setNotification(entity, time)
                if (data.isEmpty())
                    viewBinding.appointment.visibility = View.VISIBLE
                else viewBinding.appointment.visibility = View.GONE
                adapter.notifyDataSetChanged()
            }
            addAppointment.isCancelable = false
            addAppointment.show(childFragmentManager, "AddDialog")
        }

        viewBinding.onBackPress.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadData() {
        data.clear()
        data.addAll(taskDao.getAllAppointments())
    }

    private fun setNotification(entity: AppointmentEntity, time: Long) {

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

    fun setListenerItem(f: (AppointmentEntity) -> Unit) {
        listenerItem = f
    }

}