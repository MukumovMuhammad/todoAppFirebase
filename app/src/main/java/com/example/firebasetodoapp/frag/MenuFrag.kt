package com.example.firebasetodoapp.frag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.firebasetodoapp.AuthViewModel
import com.example.firebasetodoapp.R
import com.example.firebasetodoapp.databinding.FragmentMenuFragmentBinding

class MenuFrag : Fragment() {

    private lateinit var binding : FragmentMenuFragmentBinding
    private val auth : AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSeeTasks.setOnClickListener {
            findNavController().navigate(R.id.action_menuFrag_to_mainFrag)
        }


        binding.btnLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_menuFrag_to_loginFrag)
        }
    }

}