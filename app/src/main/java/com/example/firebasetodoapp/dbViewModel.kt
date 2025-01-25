package com.example.firebasetodoapp

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow

class dbViewModel() : ViewModel() {
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
        databaseRef.push().setValue(item).addOnCompleteListener {
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


    fun getTodoItems(auth: AuthViewModel, dataList: ArrayList<todoItems>) {
        val userDatas: ArrayList<todoItems> = ArrayList()

        getDbPushRef(auth)

        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               Log.i("FirebaseData", "$snapshot");

                dataList.clear()
                for (childSnapshot in snapshot.children){
                    var item = childSnapshot.getValue(todoItems::class.java)
                    Log.d("FirebaseData", "$item");
                    dataList.add(item!!)

                }
                Log.i("FirebaseData", "$userDatas");

            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("FirebaseData", "$error");
            }

        })

    }
}