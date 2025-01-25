package com.example.firebasetodoapp.frag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.firebasetodoapp.AuthState
import com.example.firebasetodoapp.AuthViewModel
import com.example.firebasetodoapp.R
import com.example.firebasetodoapp.databinding.FragmentSignUpBinding

class signUpFrag : Fragment() {


    private lateinit var binding : FragmentSignUpBinding
    private val auth : AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            auth.signUp(email, password)

            if (auth.authState.value == AuthState.Authenticated){
                it.findNavController().navigate(R.id.action_signUpFrag_to_mainFrag)
            }
        }

        binding.loginTxt.setOnClickListener {
            it.findNavController().navigate(R.id.action_signUpFrag_to_loginFrag)
        }

    }

}