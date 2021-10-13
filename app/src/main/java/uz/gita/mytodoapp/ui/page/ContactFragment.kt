package uz.gita.mytodoapp.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import uz.gita.mytodoapp.R
import uz.gita.mytodoapp.app.App
import uz.gita.mytodoapp.data.AppDatabase
import uz.gita.mytodoapp.data.entity.ContactEntity
import uz.gita.mytodoapp.databinding.FragmentContactBinding
import uz.gita.mytodoapp.ui.adapter.ContactAdapter
import uz.gita.mytodoapp.ui.dialog.ContactDialog
import uz.gita.mytodoapp.ui.dialog.EditContactDialog
import uz.gita.mytodoapp.ui.dialog.EventcontactDialog

class ContactFragment : Fragment(R.layout.fragment_contact) {
    private var _viewBinding: FragmentContactBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val data = ArrayList<ContactEntity>()
    private val contactDao = AppDatabase.getDatabase().getContactDao()
    private var listenerItem: ((ContactEntity) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _viewBinding = FragmentContactBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadData()
        _viewBinding = FragmentContactBinding.bind(view)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        if (data.isEmpty())
            viewBinding.appointment.visibility = View.VISIBLE
        else
            viewBinding.appointment.visibility = View.INVISIBLE

        val adapter = ContactAdapter(data)
        viewBinding.list.adapter = adapter
        viewBinding.list.layoutManager = LinearLayoutManager(App.instance)


        adapter.setListener { pos ->
            val bottomDialog = EventcontactDialog()
            bottomDialog.setDeleteListener {
                contactDao.delete(data[pos])
                data.removeAt(pos)
                if (data.isEmpty())
                    viewBinding.appointment.visibility = View.VISIBLE
                else viewBinding.appointment.visibility = View.GONE
                adapter.notifyItemRemoved(pos)
            }
            bottomDialog.setEditListener {
                val editContactDialog = EditContactDialog()
                val bundle = Bundle()
                bundle.putSerializable("data", data[pos])
                editContactDialog.arguments = bundle
                editContactDialog.setListener {
                    data[pos] = it
                    contactDao.update(data[pos])
                    adapter.notifyItemChanged(pos)
                }
                editContactDialog.isCancelable = false
                editContactDialog.show(childFragmentManager, "edit")
            }
            bottomDialog.show(childFragmentManager, "event")
        }
        viewBinding.buttonAdd.setOnClickListener {
            val contactDialog = ContactDialog()
            contactDialog.setDialogListener {
                contactDao.insert(it)
                data.add(it)
                if (data.isEmpty())
                    viewBinding.appointment.visibility = View.VISIBLE
                else viewBinding.appointment.visibility = View.GONE
                adapter.notifyDataSetChanged()
            }
            contactDialog.isCancelable = false
            contactDialog.show(childFragmentManager, "ContactAdded")
        }

        viewBinding.onBackPress.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun loadData() {
        data.clear()
        data.addAll(contactDao.getAllContact())
    }

    fun setListenerItem(f: (ContactEntity) -> Unit) {
        listenerItem = f
    }

}