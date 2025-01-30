package com.example.firebasetodoapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class adapterTodoRv(
    private var items: List<todoItems>,
    private val dbViewModel: DbViewModel,
    private val auth: AuthViewModel,
    private val listener: OnTodoItemClickListener
    ) : RecyclerView.Adapter<adapterTodoRv.todoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): todoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false);

        return todoViewHolder(view);
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: todoViewHolder, position: Int) {
        val currentItem = items[position];
        holder.title.text = currentItem.title;
        holder.description.text = currentItem.description;
        val resources = holder.itemView.resources

        if (currentItem.done == true){
            holder.card.setBackgroundColor(resources.getColor(R.color.grey))
            holder.title.paintFlags = holder.title.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            holder.description.paintFlags = holder.description.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
        }
        else{
            holder.card.setBackgroundColor(resources.getColor(R.color.lightBlue))
            holder.title.paintFlags = holder.title.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.description.paintFlags = holder.description.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }


        holder.itemView.setOnClickListener {
            listener.ItemClicked(currentItem);
        }

        holder.btn.setOnClickListener {
            val popup = PopupMenu(holder.itemView.context, holder.btn)
            popup.inflate(R.menu.item_options_menu)

            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_edit -> {
                        listener.onEditClicked(position, items[position])
                        true
                    }

                    R.id.action_delete -> {
                        listener.onDeleteClicked(position, items[position].id.toString())
                        true
                    }

                    else -> false
                }

            }



            popup.show()
        }
    }

    fun updateList(newList: List<todoItems>) {

        items = newList
        notifyDataSetChanged()
    }




    inner class todoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.tv_title);
        val description: TextView = itemView.findViewById(R.id.tv_description)
        val btn : ImageButton = itemView.findViewById(R.id.btn_more_options)
        val card : androidx.cardview.widget.CardView = itemView.findViewById(R.id.card)
    }

    interface OnTodoItemClickListener {
        fun onDeleteClicked(position: Int, id: String)
        fun onEditClicked(position: Int, item: todoItems)
        fun ItemClicked(item: todoItems)
    }

}