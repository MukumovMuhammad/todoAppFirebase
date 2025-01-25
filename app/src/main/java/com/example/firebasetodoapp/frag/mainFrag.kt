package com.example.firebasetodoapp.frag

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasetodoapp.AuthViewModel
import com.example.firebasetodoapp.databinding.FragmentMainBinding
import com.example.firebasetodoapp.adapterTodoRv
import com.example.firebasetodoapp.databinding.DialogTodoAddBinding
import com.example.firebasetodoapp.dbViewModel
import com.example.firebasetodoapp.todoItems
import kotlinx.coroutines.launch


class mainFrag : Fragment() {

    private lateinit var binding : FragmentMainBinding
    private lateinit var adapter: adapterTodoRv

    private val auth : AuthViewModel by viewModels()
    private  var todoList : ArrayList<todoItems> = ArrayList<todoItems>()
    private val database : dbViewModel by viewModels();

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        adapter = adapterTodoRv(todoList)
        binding.todoRv.layoutManager = LinearLayoutManager(requireContext())
        binding.todoRv.adapter = adapter

        lifecycleScope.launch {
            Thread.sleep(1000);
            database.getTodoItems(auth, todoList)
            adapter.notifyDataSetChanged()
        }

        binding.button.setOnClickListener {
            createDialog()
        }

        binding.editTextSearch.addTextChangedListener {
            val searchText = it.toString()

            searchItem(searchText);
        }


    }






    fun createDialog(){
        val builder = AlertDialog.Builder(requireContext())
        val bindingDialog = DialogTodoAddBinding.inflate(layoutInflater)

        builder.setTitle("Add todo App")
            .setView(bindingDialog.root)
            .setPositiveButton("Add") {dialog,_ ->
                val title = bindingDialog.editTextTitle.text.toString()
                val description = bindingDialog.editTextDescription.text.toString()
                val item = todoItems(title, description, false)
                addTodoItem(item)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel"){dialog,_ ->
                dialog.dismiss()
            }

        builder.create().show()
    }



    fun addTodoItem(item : todoItems){

        if (item.title == null || item.description == null){
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }


        var status : Boolean = database.pushTodoItem(item, auth)




        if (status){
            Toast.makeText(requireContext(), "task successfully added", Toast.LENGTH_SHORT).show()
            database.getTodoItems(auth, todoList)
            adapter.notifyDataSetChanged()
        }

    }



    fun searchItem(searchText: String){
        database.getTodoItems(auth, todoList)
        val searchList = ArrayList<todoItems>()

        Log.v("FirebaseData", "The todo List is $todoList")

        for (item in todoList){
            if (item.title?.contains(searchText) == true){
                searchList.add(item)
            }
        }

        adapter.updateList(searchList)
    }
}