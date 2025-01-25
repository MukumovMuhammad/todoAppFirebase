package com.example.firebasetodoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adapterTodoRv(private val items: ArrayList<todoItems>) : RecyclerView.Adapter<adapterTodoRv.todoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): todoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false);

        return todoViewHolder(view);
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: todoViewHolder, position: Int) {
        val currentItem = items[position];
        holder.title.text = currentItem.title;
        holder.description.text = currentItem.description;
    }


    inner class todoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.tv_title);
        val description: TextView = itemView.findViewById(R.id.tv_description)
        val btn : ImageButton = itemView.findViewById(R.id.btn_more_options)
    }
}