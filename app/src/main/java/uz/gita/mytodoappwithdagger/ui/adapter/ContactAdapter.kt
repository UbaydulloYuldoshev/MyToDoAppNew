package uz.gita.mytodoappwithdagger.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.gita.mytodoappwithdagger.R
import uz.gita.mytodoappwithdagger.data.entity.ContactEntity

class ContactAdapter(private val list: List<ContactEntity>) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private var listener: ((Int) -> Unit)? = null
    private var listenerSingleItem: ((Int) -> Unit)? = null
    private var listenerItemTime: ((Int) -> Unit)? = null
    private var listenerItem: ((ContactEntity) -> Unit)? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        private val textDescription: TextView = itemView.findViewById(R.id.textDescription)
        init {
            itemView.setOnLongClickListener {
                listener?.invoke(absoluteAdapterPosition)
                return@setOnLongClickListener true
            }
            itemView.setOnClickListener {
                listenerItem?.invoke(list[absoluteAdapterPosition])
            }
            itemView.setOnClickListener {
                listenerItemTime?.invoke(absoluteAdapterPosition)
            }
        }
        fun bind() {
            itemView.apply {
                val data = list[absoluteAdapterPosition]
                textTitle.text = data.name
                textDescription.text = data.phoneNumber
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int = list.size

    fun setListener(f: (Int) -> Unit) {
        listener = f
    }
    fun setListenerSingleItem(f: (Int) -> Unit) {
        listenerSingleItem = f
    }

    fun setListenerItemTime(f: (Int) -> Unit) {
        listenerItemTime = f
    }

    fun setListenerItem(f: (ContactEntity) -> Unit) {
        listenerItem = f
    }
}