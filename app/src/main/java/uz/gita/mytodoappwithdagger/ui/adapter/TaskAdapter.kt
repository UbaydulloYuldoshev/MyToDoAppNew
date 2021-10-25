package uz.gita.mytodoappwithdagger.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.gita.mytodoappwithdagger.R
import uz.gita.mytodoappwithdagger.data.entity.TaskEntity

class TaskAdapter(private val list: List<TaskEntity>) : RecyclerView.Adapter<TaskAdapter.VH>() {

    private var listener: ((Int) -> Unit)? = null
    private var listenerItemTime: ((Int) -> Unit)? = null
    private var listenerItem: ((TaskEntity) -> Unit)? = null

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
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
            selectTime.setOnClickListener {
                listenerItemTime?.invoke(absoluteAdapterPosition)
            }
        }

        fun bind() {
            itemView.apply {
                val data = list[absoluteAdapterPosition]
                textTitle.text = "Title: ${data.title}"
                textDescription.text = "Description: ${data.description}"
                selectTime.text = "${data.timeAlarm}"
                addedTime.text = "Deadline: ${data.time}"
                if (data.pagePos == 0) {
                    this.setBackgroundColor(Color.parseColor("#A80505"))

                }
                if (data.pagePos == 1) {
                    this.setBackgroundColor(Color.parseColor("#FF4C05"))
                }
                if (data.pagePos == 2) {
                    this.setBackgroundColor(Color.parseColor("#0B8F0B"))
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = list.size
    fun setListener(f: (Int) -> Unit) {
        listener = f
    }

    fun setListenerTime(f: (Int) -> Unit) {
        listener = f
    }

    fun setListenerItem(f: (TaskEntity) -> Unit) {
        listenerItem = f
    }
}