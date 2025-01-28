package com.example.firebasetodoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class adapterTodoRv(
    private val items: ArrayList<todoItems>,
    private val dbViewModel: DbViewModel,
    private val auth: AuthViewModel
    ) : RecyclerView.Adapter<adapterTodoRv.todoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): todoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false);

        return todoViewHolder(view);
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: todoViewHolder, position: Int) {
        val currentItem = items[position];
        holder.title.text = currentItem.title;
        holder.description.text = currentItem.description;


        holder.btn.setOnClickListener {
            val popup = PopupMenu(holder.itemView.context, holder.btn)
            popup.inflate(R.menu.item_options_menu)

            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_edit -> {
                        true
                    }

                    R.id.action_delete -> {
                        dbViewModel.deleteTodoItem(position, auth) {isSuccessful ->
                            if (isSuccessful){
                                Toast.makeText(holder.itemView.context, "Successfully deleted", Toast.LENGTH_SHORT).show()
                                notifyItemRemoved(position)
                            }else {
                                Toast.makeText(holder.itemView.context, "Failed to delete", Toast.LENGTH_SHORT).show()
                            }
                        }

                        true
                    }

                    else -> false
                }

            }

            popup.show()
        }
    }

    fun updateList(searchList: ArrayList<todoItems>) {
        items.clear()
        items.addAll(searchList)
        notifyDataSetChanged()
    }




    inner class todoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.tv_title);
        val description: TextView = itemView.findViewById(R.id.tv_description)
        val btn : ImageButton = itemView.findViewById(R.id.btn_more_options)
    }
}