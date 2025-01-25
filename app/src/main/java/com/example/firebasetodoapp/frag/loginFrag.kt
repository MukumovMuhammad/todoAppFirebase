package com.example.firebasetodoapp.frag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.firebasetodoapp.AuthState
import com.example.firebasetodoapp.AuthViewModel
import com.example.firebasetodoapp.R
import com.example.firebasetodoapp.databinding.FragmentLoginBinding


class loginFrag : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private val auth : AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (auth.authState.value == AuthState.Authenticated){
            findNavController().navigate(R.id.action_loginFrag_to_mainFrag)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            auth.login(email, password)

            if (auth.authState.value == AuthState.Authenticated){
                findNavController().navigate(R.id.action_loginFrag_to_mainFrag)
            }

        }


        binding.signUpTxt.setOnClickListener {
            findNavController().navigate(R.id.action_loginFrag_to_signUpFrag)
        }

    }

}


