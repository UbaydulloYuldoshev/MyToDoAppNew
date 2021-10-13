package uz.gita.mytodoapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.gita.mytodoapp.R
import uz.gita.mytodoapp.data.entity.AppointmentEntity

class AppointmentAdapter(private val list: List<AppointmentEntity>) :
    RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {

    private var listener: ((Int) -> Unit)? = null
    private var listenerSingleItem: ((Int) -> Unit)? = null
    private var listenerItemTime: ((Int) -> Unit)? = null
    private var listenerItem: ((AppointmentEntity) -> Unit)? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        private val textDescription: TextView = itemView.findViewById(R.id.textDescription)
        private val addedTime: TextView = itemView.findViewById(R.id.addedTime)
        private val selectTime: TextView = itemView.findViewById(R.id.selectTime)
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
                textTitle.text = "Meeting: ${data.title}"
                textDescription.text = "Place: ${data.place}"
                selectTime.text = data.deadlineHour
                addedTime.text = "Time: ${data.time}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment, parent, false))


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

    fun setListenerItem(f: (AppointmentEntity) -> Unit) {
        listenerItem = f
    }
}