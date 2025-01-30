package com.example.firebasetodoapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DbViewModel: ViewModel() {

    private val _todoList = MutableLiveData<List<todoItems>>()
    val todoList: LiveData<List<todoItems>> = _todoList


    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var databaseRef: DatabaseReference


    private  fun getDbRef(auth: AuthViewModel){
        databaseRef = firebaseDatabase.reference.child("Tasks").child(auth.getCurrentUser()?.uid.toString())
    }

    private fun getDbPushRef(auth: AuthViewModel){
        databaseRef = FirebaseDatabase.getInstance().getReference("Tasks").child(auth.getCurrentUser()?.uid.toString())
    }


    fun pushTodoItem(item : todoItems, auth: AuthViewModel) : Boolean{
        var status : Boolean = false
        getDbRef(auth)
        val storeRef = databaseRef.push()
        item.id = storeRef.key
        storeRef.setValue(item).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i("FirebaseData", "Successfully added")
                status = true;
            } else {
                Log.e("FirebaseData", "${it.exception?.message}")
                status = false;
            }
        }
        return status


    }


    fun deleteTodoItem(id: String, auth: AuthViewModel, callback: (Boolean) -> Unit) {
        getDbRef(auth)
        Log.v("FirebaseData", "The current path is  $databaseRef")
        Log.v("FirebaseData", "The id is $id")
        databaseRef.child(id).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.v("FirebaseData", "Successfully deleted")
                getTodoItems(auth)
                callback(true);
            } else {
               callback(false);
            }
        }
    }


    fun editTodoItem(oldItem: todoItems, newItem: todoItems, auth: AuthViewModel, callback: (Boolean) -> Unit) {

        if (newItem.title == null || newItem.description == null){
            callback(false);
            return
        }
        getDbRef(auth)
        newItem.id = oldItem.id
        databaseRef.child(oldItem.id.toString()).setValue(newItem).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.v("FirebaseData", "Successfully updated")
                getTodoItems(auth);
                callback(true);
            }
            else{
                callback(false);
            }
        }
    }




    fun getTodoItems(auth: AuthViewModel) {
        getDbPushRef(auth)
        Log.i("FirebaseData", "The get function is called !");
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               Log.i("FirebaseData", "$snapshot");

                val items = snapshot.children.map { it.getValue(todoItems::class.java)!! }
                _todoList.value = ArrayList(items)

//                for (childSnapshot in snapshot.children){
//                    var item = childSnapshot.getValue(todoItems::class.java)
//                    Log.d("FirebaseData", "$item");
//                    dataList.add(item!!)
//
//                }
                Log.i("FirebaseData", " We got the data : $items");

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "There is error in getting data : $error");
            }

        })

    }


    fun todoItemIsClicked(item: todoItems,  auth: AuthViewModel, callback: (Boolean) -> Unit){
        item.done = !item.done!!
        databaseRef.child(item.id.toString()).setValue(item).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.v("FirebaseData", "Successfully updated")
                getTodoItems(auth);
                callback(true);
            }
            else{
                callback(false);
            }
        }

    }
}