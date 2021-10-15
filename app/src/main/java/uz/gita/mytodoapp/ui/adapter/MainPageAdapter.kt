package uz.gita.mytodoapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainPageAdapter(fg:FragmentManager,lifecycle: Lifecycle, private val todoPage: Fragment, private val doingPage: Fragment,
    private val donePage: Fragment
) : FragmentStateAdapter(fg,lifecycle) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                todoPage
            }
            1 -> {
                doingPage
            }

            else -> {
               donePage
            }
        }
    }
}