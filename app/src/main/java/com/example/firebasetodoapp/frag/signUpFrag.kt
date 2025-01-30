package com.example.firebasetodoapp.frag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.firebasetodoapp.AuthState
import com.example.firebasetodoapp.AuthViewModel
import com.example.firebasetodoapp.R
import com.example.firebasetodoapp.databinding.FragmentSignUpBinding
import kotlinx.coroutines.launch

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

        binding.etEmail.setOnClickListener{
            binding.tvError.text = "";
        }

        binding.etPassword.setOnClickListener{
            binding.tvError.text = "";
        }


        auth.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthState.Authenticated -> {
                    findNavController().navigate(R.id.action_signUpFrag_to_menuFrag)
                }

                is AuthState.Error -> {
                    binding.tvError.text = state.message
                }

                else -> {}
            }
        }

        binding.btnSignUp.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            auth.signUp(email, password)


        }

        binding.loginTxt.setOnClickListener {
            it.findNavController().navigate(R.id.action_signUpFrag_to_loginFrag)
        }

    }

}