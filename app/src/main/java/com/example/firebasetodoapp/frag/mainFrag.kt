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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasetodoapp.AuthViewModel
import com.example.firebasetodoapp.R
import com.example.firebasetodoapp.databinding.FragmentMainBinding
import com.example.firebasetodoapp.adapterTodoRv
import com.example.firebasetodoapp.databinding.DialogTodoAddBinding
import com.example.firebasetodoapp.DbViewModel
import com.example.firebasetodoapp.todoItems


class mainFrag : Fragment(), adapterTodoRv.OnTodoItemClickListener {

    private lateinit var binding : FragmentMainBinding
    private lateinit var adapter: adapterTodoRv

    private val auth : AuthViewModel by viewModels()
    private lateinit var todoList : List<todoItems>
    private val database : DbViewModel by viewModels();

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

        database.todoList.observe(viewLifecycleOwner){ items ->
            Log.i("FirebaseData", "todoList data changed! The item is $items");
            todoList = items
            adapter.updateList(items)
        }

        todoList = database.todoList.value ?: emptyList()
        adapter = adapterTodoRv(todoList, database, auth, this)
        binding.todoRv.layoutManager = LinearLayoutManager(requireContext())
        binding.todoRv.adapter = adapter

        database.getTodoItems(auth)

//        lifecycleScope.launch {
//            Thread.sleep(1000);
//            database.getTodoItems(auth, todoList)
//            adapter.notifyDataSetChanged()
//        }






        binding.button.setOnClickListener {
            createAddDialog()
        }

        binding.editTextSearch.addTextChangedListener {
            val searchText = it.toString()

            searchItem(searchText);
        }


        binding.imcBack.setOnClickListener{
            findNavController().navigate(R.id.action_mainFrag_to_menuFrag)
        }


    }






    fun createAddDialog(){
        val builder = AlertDialog.Builder(requireContext())
        val bindingDialog = DialogTodoAddBinding.inflate(layoutInflater)

        builder.setTitle("Add todo App")
            .setView(bindingDialog.root)
            .setPositiveButton("Add") {dialog,_ ->
                val title = bindingDialog.editTextTitle.text.toString()
                val description = bindingDialog.editTextDescription.text.toString()
                val item = todoItems(todoList.size.toString(), title, description, false)
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
            database.getTodoItems(auth)
            adapter.notifyDataSetChanged()
        }

    }



    fun searchItem(searchText: String){
        database.getTodoItems(auth)
        val searchList = ArrayList<todoItems>()

        Log.v("FirebaseData", "The todo List is $todoList")

        for (item in todoList){
            if (item.title?.contains(searchText) == true){
                searchList.add(item)
            }
        }

        adapter.updateList(searchList)
    }

    override fun onDeleteClicked(position: Int, id: String) {
        database.deleteTodoItem(id, auth) { isSuccessful ->
            if (isSuccessful){
                Toast.makeText(requireContext(), "Successfully deleted", Toast.LENGTH_SHORT).show()
                adapter.notifyItemRemoved(position)
            }else {
                Toast.makeText(requireContext(), "Failed to delete", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditClicked(position: Int, oldItem: todoItems) {
        val builder = AlertDialog.Builder(requireContext())
        val bindingDialog = DialogTodoAddBinding.inflate(layoutInflater)

        bindingDialog.editTextTitle.setText(oldItem.title)
        bindingDialog.editTextDescription.setText(oldItem.description)


        builder.setTitle("Edit todo ${oldItem.title}")
            .setView(bindingDialog.root)
            .setPositiveButton("Edit") {dialog,_ ->
                val title = bindingDialog.editTextTitle.text.toString()
                val description = bindingDialog.editTextDescription.text.toString()
                val newItem = todoItems(todoList.size.toString(), title, description, false)
                database.editTodoItem(oldItem ,newItem, auth) { isSuccessful ->
                    if (isSuccessful){
                        Toast.makeText(requireContext(), "Successfully updated", Toast.LENGTH_SHORT).show()
                        adapter.notifyItemChanged(position)
                    }
                    else {
                        Toast.makeText(requireContext(), "Failed to update", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel"){dialog,_ ->
                dialog.dismiss()
            }

        builder.create().show()
    }

    override fun ItemClicked(item: todoItems) {
        database.todoItemIsClicked(item, auth) { isSuccessful ->
          if(!isSuccessful){
              Toast.makeText(requireContext(), "Failed to update", Toast.LENGTH_SHORT).show()
          }
        }
    }
}