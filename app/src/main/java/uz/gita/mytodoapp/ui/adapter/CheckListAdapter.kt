package uz.gita.mytodoapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import uz.gita.mytodoapp.R
import uz.gita.mytodoapp.model.CheckData


class CheckListAdapter(private val list: List<CheckData>) : RecyclerView.Adapter<CheckListAdapter.CheckListViewHolder>() {
    private var closeItemListener : ((Int) -> Unit)?=null

    inner class CheckListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.buttonCheck)
        private val editText: EditText = itemView.findViewById(R.id.editText)
        private val close: ImageView = itemView.findViewById(R.id.buttonClose)
        init {
            checkBox.setOnClickListener {
                if (list[adapterPosition].isChecked == 0)
                    list[adapterPosition].isChecked = 1
                else list[adapterPosition].isChecked = 0
            }
            editText.addTextChangedListener {
                it?.let {
                    list[adapterPosition].text = it.toString()
                }
            }
            close.setOnClickListener {
                closeItemListener?.invoke(adapterPosition)
            }
        }
        fun bind() {
            val data = list[adapterPosition]
            editText.setText(data.text)
            checkBox.isChecked = data.isChecked != 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CheckListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_check_list, parent, false))

    override fun onBindViewHolder(holder: CheckListViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = list.size

    fun setCloseItemListener(f : (Int) -> Unit) {
        closeItemListener = f
    }
}
