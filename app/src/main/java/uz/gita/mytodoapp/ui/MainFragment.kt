package uz.gita.mytodoapp.ui

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import uz.gita.mytodoapp.AlarmReceiver
import uz.gita.mytodoapp.DepthPageTransformer
import uz.gita.mytodoapp.R
import uz.gita.mytodoapp.data.AppDatabase
import uz.gita.mytodoapp.data.entity.TaskEntity
import uz.gita.mytodoapp.databinding.ActivityMainNavBinding
import uz.gita.mytodoapp.ui.adapter.MainPageAdapter
import uz.gita.mytodoapp.ui.dialog.AddTaskDialog
import uz.gita.mytodoapp.ui.page.DoingPage
import uz.gita.mytodoapp.ui.page.DonePage
import uz.gita.mytodoapp.ui.page.TodoPage
import java.lang.Exception

class MainFragment : Fragment(R.layout.activity_main_nav) {
    private var _viewBinding: ActivityMainNavBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val taskDao = AppDatabase.getDatabase().getTaskDao()
    private var pos = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _viewBinding = ActivityMainNavBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createNotificationChannel()

        viewBinding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.about -> {
                    findNavController().navigate(R.id.action_mainFragment_to_appointmentFragment)
                    viewBinding.navDrawer.closeDrawer(GravityCompat.START)
                }
                R.id.share -> {
                    try {
                        val i = Intent(Intent.ACTION_SEND)
                        i.type = "text/plain"
                        i.putExtra(Intent.EXTRA_SUBJECT, "Your Subject")
                        var sAux = "\nLet me recommend you this application\n\n"
                        sAux = """${sAux}https://play.google.com/store/apps/details?id=uz.gita.mytodoapp        
                        """.trimIndent()
                        i.putExtra(Intent.EXTRA_TEXT, sAux)
                        startActivity(Intent.createChooser(i, "choose one"))
                    } catch (e: Exception) {
                        Log.e(">>>", "Error: $e")
                    }
                    viewBinding.navDrawer.closeDrawer(GravityCompat.START)

                }
                R.id.calendar -> {
                    findNavController().navigate(R.id.action_mainFragment_to_contactFragment)
                    viewBinding.navDrawer.closeDrawer(GravityCompat.START)

                }
            }
            true
        }
        viewBinding.include.imageHome.setOnClickListener {
            viewBinding.navDrawer.openDrawer(GravityCompat.START)
        }

        val todoPage = TodoPage()
        val doingPage = DoingPage()
        val donePage = DonePage()
        val adapter = MainPageAdapter(childFragmentManager,lifecycle,
            todoPage, doingPage, donePage)
        adapter.notifyDataSetChanged()

        viewBinding.include.pager.adapter = adapter
        TabLayoutMediator(viewBinding.include.tabLayout,
            viewBinding.include.pager) { tab, position ->
            pos = position
            when (position) {
                0 -> tab.text = "Todo"
                1 -> tab.text = "Doing"
                else -> tab.text = "Done"
            }
        }.attach()

        viewBinding.include.pager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0)
                    viewBinding.include.mainBtn.visibility = View.VISIBLE
                if (position == 1)
                    viewBinding.include.mainBtn.visibility = View.INVISIBLE
                if (position == 2)
                    viewBinding.include.mainBtn.visibility = View.INVISIBLE
            }
        })
        viewBinding.include.pager.setPageTransformer(DepthPageTransformer())

        viewBinding.include.buttonAdd.setOnClickListener {
            val addDialog = AddTaskDialog()
            addDialog.setDialogListener { entity, time ->
                taskDao.insert(entity)
                todoPage.loadData()
                setNotification(entity, time)
                adapter.notifyDataSetChanged()
            }
            addDialog.isCancelable = false
            addDialog.show(childFragmentManager, "AddDialog")
        }
        todoPage.setListenerItem {
            val bundle = Bundle()
            bundle.putSerializable("data", it)
            findNavController().navigate(R.id.action_mainFragment_to_infoFragment, bundle)
        }

        todoPage.setUpdateDoingPageListener {
            doingPage.loadData()
        }
        donePage.setListenerItem {
            val bundle = Bundle()
            bundle.putSerializable("data", it)
            findNavController().navigate(R.id.action_mainFragment_to_infoFragment, bundle)
        }
        donePage.setUpdateDoingPageListener {
            doingPage.loadData()
        }
        doingPage.setListenerItem {
            val bundle = Bundle()
            bundle.putSerializable("data", it)
            findNavController().navigate(R.id.action_mainFragment_to_infoFragment, bundle)
        }
        doingPage.setUpdateDonePageListener {
            donePage.loadData()
        }
        doingPage.setUpdateTodoPageListener {
            todoPage.loadData()
        }
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
